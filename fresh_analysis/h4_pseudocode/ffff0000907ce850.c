
bool FUN_ffff0000907ce850(uint param_1)

{
  undefined4 uVar1;
  int iVar2;
  long lVar3;
  undefined *puVar4;
  
  lVar3 = FUN_ffff000090741bdc(s_usb2jtag_ffff0000907f5b82);
  if (lVar3 == 0) {
    uVar1 = 0;
  }
  else {
    FUN_ffff000090741bdc(s_usb2jtag_ffff0000907f5b82);
    uVar1 = FUN_ffff00009077d72c();
  }
  FUN_ffff00009077e11c
            (s__s__d___USB2JTAG__current_settin_ffff0000907d71a1,s_set_usb2jtag_ffff0000907fca08,
             0x26,uVar1);
  puVar4 = (&PTR_DAT_ffff000090838ac0)[param_1];
  iVar2 = FUN_ffff000090741cb8(s_usb2jtag_ffff0000907f5b82,puVar4);
  if (iVar2 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___USB2JTAG_set_USB2JTAG__s_ffff0000907d71cb,s_set_usb2jtag_ffff0000907fca08,
               0x28,puVar4);
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d___USB2JTAG_set_USB2JTAG__s_ffff0000907d3ce7,s_set_usb2jtag_ffff0000907fca08,
               0x2a,puVar4);
  }
  return iVar2 != 0;
}


