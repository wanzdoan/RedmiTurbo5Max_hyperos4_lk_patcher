
undefined8 FUN_ffff0000be018e08(void)

{
  undefined8 uVar1;
  
  FUN_ffff0000be05fd98
            (s__s__d__call_udc_init____USB_VID__ffff0000be0a6ace,s_usb_init_ffff0000be0c2aa2,0xb7,
             DAT_ffff0000be0d6160,DAT_ffff0000be0d6162);
  DAT_ffff0000be0d6178 = FUN_ffff0000be014814();
  FUN_ffff0000be068fb0(&DAT_ffff0000be0d6160);
  FUN_ffff0000be01a4c4(&DAT_ffff0000be108868,0,1);
  FUN_ffff0000be01a4c4(&DAT_ffff0000be108828,0,1);
  DAT_ffff0000be108860 = FUN_ffff0000be06927c(0x82,0x200);
  if (DAT_ffff0000be108860 != 0) {
    DAT_ffff0000be108820 = FUN_ffff0000be06927c(2,0x200);
    if (DAT_ffff0000be108820 != 0) {
      DAT_ffff0000be108898 = DAT_ffff0000be108860;
      DAT_ffff0000be1088a0 = DAT_ffff0000be108820;
      DAT_ffff0000be108818 = FUN_ffff0000be0694a4();
      if (DAT_ffff0000be108818 != 0) {
        uVar1 = FUN_ffff0000be0695b0(&PTR_LAB_ffff0000be0d6180);
        if ((int)uVar1 == 0) {
          return uVar1;
        }
        thunk_FUN_ffff0000be09937c(DAT_ffff0000be108818);
      }
      FUN_ffff0000be069488(DAT_ffff0000be108820);
    }
    FUN_ffff0000be069488(DAT_ffff0000be108860);
  }
  return 0xffffffff;
}


