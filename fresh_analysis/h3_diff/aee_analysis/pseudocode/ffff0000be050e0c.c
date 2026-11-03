
void FUN_ffff0000be050e0c(undefined8 param_1)

{
  char *pcVar1;
  int iVar2;
  undefined8 uVar3;
  
  uVar3 = FUN_ffff0000be060e5c(&DAT_ffff0000be0bceec);
  iVar2 = FUN_ffff0000be060f54(param_1,&DAT_ffff0000be0bceec,uVar3);
  if (iVar2 == 0) {
    FUN_ffff0000be05fd98(s__FASTBOOT__Enable_USB2JTAG_ffff0000be0abd37);
    FUN_ffff0000be013738(s_Enable_USB2JTAG_ffff0000be0aee50);
    FUN_ffff0000be09b7f0(1);
  }
  else {
    uVar3 = FUN_ffff0000be060e5c(&DAT_ffff0000be0c0595);
    iVar2 = FUN_ffff0000be060f54(param_1,&DAT_ffff0000be0c0595,uVar3);
    if (iVar2 == 0) {
      FUN_ffff0000be05fd98(s__FASTBOOT__Disable_USB2JTAG_ffff0000be0b9168);
      FUN_ffff0000be013738(s_Disable_USB2JTAG_ffff0000be0c67e5);
      FUN_ffff0000be09b7f0(0);
    }
    else {
      iVar2 = FUN_ffff0000be09b784();
      pcVar1 = s_Disable_ffff0000be0ab5b2;
      if (iVar2 != 0) {
        pcVar1 = s_Enable_ffff0000be0a874b;
      }
      FUN_ffff0000be05fd98(s__FASTBOOT__Current_USB2JAG_setti_ffff0000be0b4881,pcVar1);
    }
  }
  FUN_ffff0000be05fd98(s__FASTBOOT__USB2JTAG_Done__ffff0000be0c479a);
  FUN_ffff0000be013828(&DAT_ffff0000be0ae340);
  FUN_ffff0000be0356b4(0);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be020454(1,7);
}


