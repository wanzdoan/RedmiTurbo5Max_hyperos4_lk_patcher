
undefined8 FUN_ffff0000907bd2a4(undefined8 param_1,long param_2)

{
  undefined4 uVar1;
  int iVar2;
  undefined8 uVar3;
  undefined *puVar4;
  char *pcVar5;
  
  thunk_FUN_ffff00009077eb20(param_2,param_1,0x40);
  uVar1 = FUN_ffff0000907c1050(*(undefined4 *)(param_2 + 4));
  *(undefined4 *)(param_2 + 4) = uVar1;
  uVar1 = FUN_ffff0000907c1050(*(undefined4 *)(param_2 + 8));
  *(undefined4 *)(param_2 + 8) = uVar1;
  uVar3 = FUN_ffff0000907c1060(*(undefined8 *)(param_2 + 0xc));
  *(undefined8 *)(param_2 + 0xc) = uVar3;
  uVar3 = FUN_ffff0000907c1060(*(undefined8 *)(param_2 + 0x14));
  *(undefined8 *)(param_2 + 0x14) = uVar3;
  uVar3 = FUN_ffff0000907c1060(*(undefined8 *)(param_2 + 0x1c));
  *(undefined8 *)(param_2 + 0x1c) = uVar3;
  iVar2 = FUN_ffff0000907c1080(param_2,&DAT_ffff0000907f76fd,4);
  if (iVar2 == 0) {
    if (*(uint *)(param_2 + 4) < 2) {
      return 1;
    }
    uVar3 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_footer_c_ffff0000907d2345);
    puVar4 = &DAT_ffff0000907d8a4e;
    pcVar5 = s_No_support_for_footer_version__ffff0000907f7702;
  }
  else {
    uVar3 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_footer_c_ffff0000907d2345);
    puVar4 = &DAT_ffff0000907fe6d1;
    pcVar5 = s_Footer_magic_is_incorrect__ffff0000907fe6d4;
  }
  FUN_ffff0000907c0ee8(uVar3,&DAT_ffff0000907ebfa6,puVar4,s___ERROR__ffff0000907e07dc,pcVar5,0);
  return 0;
}


