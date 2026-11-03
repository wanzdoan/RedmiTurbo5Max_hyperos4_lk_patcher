// ffff00009070fd00  size=276  FUN_ffff00009070fd00

undefined8 FUN_ffff00009070fd00(undefined8 param_1,undefined8 param_2)

{
  int iVar1;
  char *pcVar2;
  undefined8 uVar3;
  
  iVar1 = FUN_ffff00009077f080(param_1,s_ANDROID__ffff0000907ebdf8,8);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff00009077f080(param_2,s_init_boot_ffff0000907f24c6,10);
    if (iVar1 == 0) {
      FUN_ffff00009077e858(0xffff00009083e100,param_1,0x630);
      pcVar2 = s__s__d__init_boot_image_header_lo_ffff0000907cf172;
      uVar3 = 0x45;
    }
    else {
      FUN_ffff00009077e858(0xffff00009083e730,param_1,0x67c);
      pcVar2 = s__s__d__boot_image_header_loaded_ffff0000907df371;
      uVar3 = 0x48;
    }
  }
  else {
    iVar1 = FUN_ffff00009077f080(param_1,s_VNDRBOOT_ffff0000907df392,8);
    if (iVar1 != 0) {
      FUN_ffff00009077de54
                (s__s__d__invalid_boot_image_header_ffff0000907e418a,
                 s_load_bootinfo_from_hdr_ffff0000907e8be7,0x4f);
      return 0xffffffff;
    }
    FUN_ffff00009077e858(0xffff00009083edb0,param_1,0x850);
    pcVar2 = s__s__d__vendor_boot_image_header_l_ffff0000907f7a3a;
    uVar3 = 0x4d;
  }
  FUN_ffff00009077de54(pcVar2,s_load_bootinfo_from_hdr_ffff0000907e8be7,uVar3);
  return 0;
}


