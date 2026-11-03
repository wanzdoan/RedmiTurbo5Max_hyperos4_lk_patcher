
void FUN_ffff000090767b18(undefined8 param_1)

{
  char *pcVar1;
  int iVar2;
  undefined8 uVar3;
  
  uVar3 = FUN_ffff00009077f250(&DAT_ffff0000907f3fed);
  iVar2 = FUN_ffff00009077f348(param_1,&DAT_ffff0000907f3fed,uVar3);
  if (iVar2 == 0) {
    FUN_ffff00009077e11c(s__FASTBOOT__Enable_USB2JTAG_ffff0000907db6e7);
    FUN_ffff000090709bec(s_Enable_USB2JTAG_ffff0000907e0245);
    FUN_ffff0000907ce850(1);
  }
  else {
    uVar3 = FUN_ffff00009077f250(&DAT_ffff0000907f938c);
    iVar2 = FUN_ffff00009077f348(param_1,&DAT_ffff0000907f938c,uVar3);
    if (iVar2 == 0) {
      FUN_ffff00009077e11c(s__FASTBOOT__Disable_USB2JTAG_ffff0000907eebaf);
      FUN_ffff000090709bec(s_Disable_USB2JTAG_ffff000090802e03);
      FUN_ffff0000907ce850(0);
    }
    else {
      iVar2 = FUN_ffff0000907ce7e4();
      pcVar1 = s_Disable_ffff0000907dabe0;
      if (iVar2 != 0) {
        pcVar1 = s_Enable_ffff0000907d6b4c;
      }
      FUN_ffff00009077e11c(s__FASTBOOT__Current_USB2JAG_setti_ffff0000907e83dc,pcVar1);
    }
  }
  FUN_ffff00009077e11c(s__FASTBOOT__USB2JTAG_Done__ffff0000907ffbb2);
  FUN_ffff000090709cdc(&DAT_ffff0000907df338);
  FUN_ffff000090745fd8(0);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000907227e0(1,7);
}


