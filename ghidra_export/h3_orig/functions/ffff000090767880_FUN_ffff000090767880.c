// ffff000090767880  size=248  FUN_ffff000090767880

void FUN_ffff000090767880(undefined8 param_1)

{
  char *pcVar1;
  int iVar2;
  undefined8 uVar3;
  
  uVar3 = FUN_ffff00009077ef88(&DAT_ffff0000907f3e61);
  iVar2 = FUN_ffff00009077f080(param_1,&DAT_ffff0000907f3e61,uVar3);
  if (iVar2 == 0) {
    FUN_ffff00009077de54(s__FASTBOOT__Enable_USB2JTAG_ffff0000907db5dc);
    FUN_ffff000090709c60(s_Enable_USB2JTAG_ffff0000907e01a2);
    FUN_ffff0000907ce1dc(1);
  }
  else {
    uVar3 = FUN_ffff00009077ef88(&DAT_ffff0000907f91f0);
    iVar2 = FUN_ffff00009077f080(param_1,&DAT_ffff0000907f91f0,uVar3);
    if (iVar2 == 0) {
      FUN_ffff00009077de54(s__FASTBOOT__Disable_USB2JTAG_ffff0000907eea3e);
      FUN_ffff000090709c60(s_Disable_USB2JTAG_ffff000090802bff);
      FUN_ffff0000907ce1dc(0);
    }
    else {
      iVar2 = FUN_ffff0000907ce170();
      pcVar1 = s_Disable_ffff0000907daad5;
      if (iVar2 != 0) {
        pcVar1 = s_Enable_ffff0000907d6ab6;
      }
      FUN_ffff00009077de54(s__FASTBOOT__Current_USB2JAG_setti_ffff0000907e8298,pcVar1);
    }
  }
  FUN_ffff00009077de54(s__FASTBOOT__USB2JTAG_Done__ffff0000907ffa0a);
  FUN_ffff000090709d50(&DAT_ffff0000907df295);
  FUN_ffff000090745d40(0);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009072259c(1,7);
}


