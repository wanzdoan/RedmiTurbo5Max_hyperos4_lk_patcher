
void FUN_ffff0000be09b8bc(void)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  
  lVar2 = FUN_ffff0000be031c40(s_usb2jtag_ffff0000be0bde50);
  if (lVar2 == 0) {
    FUN_ffff0000be05fd98
              (s__s__d___USB2JTAG__current_settin_ffff0000be0a8b26,s_get_usb2jtag_ffff0000be0a7906,
               0x1a,0);
  }
  else {
    FUN_ffff0000be031c40(s_usb2jtag_ffff0000be0bde50);
    iVar1 = FUN_ffff0000be05f3d4();
    FUN_ffff0000be05fd98
              (s__s__d___USB2JTAG__current_settin_ffff0000be0a8b26,s_get_usb2jtag_ffff0000be0a7906,
               0x1a,iVar1);
    if (iVar1 == 1) {
      pcVar3 = s_usb2jtag_mode_1_ffff0000be0a7921;
      goto LAB_ffff0000be09b944;
    }
  }
  pcVar3 = s_usb2jtag_mode_0_ffff0000be0a5482;
LAB_ffff0000be09b944:
  FUN_ffff0000be01bb50(pcVar3);
  return;
}


