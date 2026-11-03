
bool FUN_ffff0000be09b7f0(uint param_1)

{
  undefined4 uVar1;
  int iVar2;
  long lVar3;
  undefined *puVar4;
  
  lVar3 = FUN_ffff0000be031c40(s_usb2jtag_ffff0000be0bde50);
  if (lVar3 == 0) {
    uVar1 = 0;
  }
  else {
    FUN_ffff0000be031c40(s_usb2jtag_ffff0000be0bde50);
    uVar1 = FUN_ffff0000be05f3d4();
  }
  FUN_ffff0000be05fd98
            (s__s__d___USB2JTAG__current_settin_ffff0000be0a8b26,s_set_usb2jtag_ffff0000be0c2759,
             0x26,uVar1);
  puVar4 = (&PTR_DAT_ffff0000be0f4b80)[param_1];
  iVar2 = FUN_ffff0000be031d1c(s_usb2jtag_ffff0000be0bde50,puVar4);
  if (iVar2 == 0) {
    FUN_ffff0000be05fd98
              (s__s__d___USB2JTAG_set_USB2JTAG__s_ffff0000be0a8b50,s_set_usb2jtag_ffff0000be0c2759,
               0x28,puVar4);
  }
  else {
    FUN_ffff0000be05fd98
              (s__s__d___USB2JTAG_set_USB2JTAG__s_ffff0000be0a675e,s_set_usb2jtag_ffff0000be0c2759,
               0x2a,puVar4);
  }
  return iVar2 != 0;
}


