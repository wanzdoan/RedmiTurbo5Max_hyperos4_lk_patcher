
undefined8 FUN_ffff00009070fd78(undefined8 param_1,undefined8 param_2)

{
  int iVar1;
  char *pcVar2;
  undefined8 uVar3;
  
  iVar1 = FUN_ffff00009077f348(param_1,s_ANDROID__ffff0000907ebf7b,8);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff00009077f348(param_2,s_init_boot_ffff0000907f2652,10);
    if (iVar1 == 0) {
      FUN_ffff00009077eb20(&DAT_ffff00009083e138,param_1,0x630);
      pcVar2 = s__s__d__init_boot_image_header_lo_ffff0000907cf172;
      uVar3 = 0x45;
    }
    else {
      FUN_ffff00009077eb20(&DAT_ffff00009083e768,param_1,0x67c);
      pcVar2 = s__s__d__boot_image_header_loaded_ffff0000907df414;
      uVar3 = 0x48;
    }
  }
  else {
    iVar1 = FUN_ffff00009077f348(param_1,s_VNDRBOOT_ffff0000907df435,8);
    if (iVar1 != 0) {
      FUN_ffff00009077e11c
                (s__s__d__invalid_boot_image_header_ffff0000907e425b,
                 s_load_bootinfo_from_hdr_ffff0000907e8d2b,0x4f);
      return 0xffffffff;
    }
    FUN_ffff00009077eb20(&DAT_ffff00009083ede8,param_1,0x850);
    pcVar2 = s__s__d__vendor_boot_image_header_l_ffff0000907f7bc4;
    uVar3 = 0x4d;
  }
  FUN_ffff00009077e11c(pcVar2,s_load_bootinfo_from_hdr_ffff0000907e8d2b,uVar3);
  return 0;
}


