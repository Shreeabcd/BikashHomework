import React from "react";
import './Header.css'
import {Button} from "@material-ui/core";

const Header = (props) => {
  return (
    <div className="main-header-container">
     
      &nbsp;&nbsp;
      <span id="header-title"> Spring JWT Security </span>
      <Button
        id="session-btn"
        variant="contained"
        color={props.isLogin ? "secondary" : "primary"}
        size="medium"
        onClick={() => {
          if (props.isLogin)
            props.handleLogout();
          else
            props.openModel();
        }}
      >
        {props.isLogin? "Logout" : "Login"}
      </Button>


    </div>
  );
};

export default Header;
