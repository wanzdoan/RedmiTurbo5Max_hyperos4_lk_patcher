// ffff0000907bcc30  size=276  FUN_ffff0000907bcc30

undefined8 FUN_ffff0000907bcc30(undefined8 param_1,long param_2)

{
  undefined4 uVar1;
  int iVar2;
  undefined8 uVar3;
  undefined *puVar4;
  char *pcVar5;
  
  thunk_FUN_ffff00009077e858(param_2,param_1,0x40);
  uVar1 = FUN_ffff0000907c09dc(*(undefined4 *)(param_2 + 4));
  *(undefined4 *)(param_2 + 4) = uVar1;
  uVar1 = FUN_ffff0000907c09dc(*(undefined4 *)(param_2 + 8));
  *(undefined4 *)(param_2 + 8) = uVar1;
  uVar3 = FUN_ffff0000907c09ec(*(undefined8 *)(param_2 + 0xc));
  *(undefined8 *)(param_2 + 0xc) = uVar3;
  uVar3 = FUN_ffff0000907c09ec(*(undefined8 *)(param_2 + 0x14));
  *(undefined8 *)(param_2 + 0x14) = uVar3;
  uVar3 = FUN_ffff0000907c09ec(*(undefined8 *)(param_2 + 0x1c));
  *(undefined8 *)(param_2 + 0x1c) = uVar3;
  iVar2 = FUN_ffff0000907c0a0c(param_2,&DAT_ffff0000907f7573,4);
  if (iVar2 == 0) {
    if (*(uint *)(param_2 + 4) < 2) {
      return 1;
    }
    uVar3 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_footer_c_ffff0000907d2308);
    puVar4 = &DAT_ffff0000907d89cd;
    pcVar5 = s_No_support_for_footer_version__ffff0000907f7578;
  }
  else {
    uVar3 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_footer_c_ffff0000907d2308);
    puVar4 = &DAT_ffff0000907fe529;
    pcVar5 = s_Footer_magic_is_incorrect__ffff0000907fe52c;
  }
  FUN_ffff0000907c0874(uVar3,&DAT_ffff0000907ebe23,puVar4,s___ERROR__ffff0000907e0739,pcVar5,0);
  return 0;
}


