package id.renner.web.library;

public class PathRouter {

    // method types
    // path
    // path elements (/thing/{id])

    // tree structure

    // get
    // input path
    // split into tokens
    // for each token match tree node
    // if treenode matches, check if end node or middle node
    // if end node -> return function
    // if middle node -> repeat process for next token in next child node
    // if path can´t be matched -> error handling/null return

    // insert
    // input path and function
    // split path into functions
    // for each token match tree node
    // if token doesn´t match any, create node for token
    // if last token set to end node and put function

    // how to handle multiple methods
    // identical paths can have multiple mappings, one for each http method maximum
    // group by routes or methods?
    // routes means in each end node there will be a mapping for each method type
    // methods mean there will be a tree for each method type used, leading to duplicate mappings across trees
    // groupings by routes seems cleaner

    // how to handle path elements
    // replace with wildcard?
    // does specific mapping take precedence over wildcard? should this even be allowed?
    // if replaced with wildcard how is information about element key get preserved?
    // maybe create special marker for this(alternatively abstract to something less hacky)?

}