
undefined8 FUN_ffff00009070f8c0(undefined8 param_1,undefined8 param_2)

{
  int iVar1;
  char *pcVar2;
  undefined8 uVar3;

  iVar1 = FUN_ffff00009077fc2c(param_1,s_ANDROID__ffff0000907e4ab6,8);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff00009077fc2c(param_2,s_init_boot_ffff0000907eb20c,10);
    if (iVar1 == 0) {
      FUN_ffff00009077f488(&DAT_ffff000090836100,param_1,0x630);
      pcVar2 = s__s__d__init_boot_image_header_lo_ffff0000907c7172;
      uVar3 = 0x45;
    }
    else {
      FUN_ffff00009077f488(&DAT_ffff000090836730,param_1,0x67c);
      pcVar2 = s__s__d__boot_image_header_loaded_ffff0000907d796a;
      uVar3 = 0x48;
    }
  }
  else {
    iVar1 = FUN_ffff00009077fc2c(param_1,s_VNDRBOOT_ffff0000907d798b,8);
    if (iVar1 != 0) {
      FUN_ffff00009077ea84
                (s__s__d__invalid_boot_image_header_ffff0000907dc8f6,
                 s_load_bootinfo_from_hdr_ffff0000907e15b9,0x4f);
      return 0xffffffff;
    }
    FUN_ffff00009077f488(&DAT_ffff000090836db0,param_1,0x850);
    pcVar2 = s__s__d__vendor_boot_image_header_l_ffff0000907f0890;
    uVar3 = 0x4d;
  }
  FUN_ffff00009077ea84(pcVar2,s_load_bootinfo_from_hdr_ffff0000907e15b9,uVar3);
  return 0;
}
