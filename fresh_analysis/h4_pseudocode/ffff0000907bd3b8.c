
undefined8 FUN_ffff0000907bd3b8(undefined8 param_1,long *param_2)

{
  undefined4 uVar1;
  ulong uVar2;
  long lVar3;
  undefined8 uVar4;
  undefined *puVar5;
  char *pcVar6;
  ulong local_28;
  
  thunk_FUN_ffff00009077eb20(param_2,param_1,0x84);
  uVar2 = FUN_ffff0000907bcde8(param_1,param_2);
  if ((uVar2 & 1) != 0) {
    if (*param_2 == 2) {
      lVar3 = FUN_ffff0000907c1060(param_2[2]);
      param_2[2] = lVar3;
      uVar1 = FUN_ffff0000907c1050((int)param_2[7]);
      *(undefined4 *)(param_2 + 7) = uVar1;
      uVar1 = FUN_ffff0000907c1050(*(undefined4 *)((long)param_2 + 0x3c));
      *(undefined4 *)((long)param_2 + 0x3c) = uVar1;
      uVar1 = FUN_ffff0000907c1050((int)param_2[8]);
      *(undefined4 *)(param_2 + 8) = uVar1;
      uVar1 = FUN_ffff0000907c1050(*(undefined4 *)((long)param_2 + 0x44));
      *(undefined4 *)((long)param_2 + 0x44) = uVar1;
      local_28 = 0x74;
      uVar2 = FUN_ffff0000907c112c(&local_28,(int)param_2[7]);
      if ((((uVar2 & 1) == 0) ||
          (uVar2 = FUN_ffff0000907c112c(&local_28,*(undefined4 *)((long)param_2 + 0x3c)),
          (uVar2 & 1) == 0)) ||
         (uVar2 = FUN_ffff0000907c112c(&local_28,(int)param_2[8]), (uVar2 & 1) == 0)) {
        uVar4 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_hash_des_ffff0000907d54ce);
        puVar5 = &DAT_ffff0000907ea457;
        pcVar6 = s_Overflow_while_adding_up_sizes__ffff0000907e8a80;
      }
      else {
        if (local_28 <= (ulong)param_2[1]) {
          return 1;
        }
        uVar4 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_hash_des_ffff0000907d54ce);
        puVar5 = &DAT_ffff0000907f3d11;
        pcVar6 = s_Descriptor_payload_size_overflow_ffff0000907ef1c6;
      }
    }
    else {
      uVar4 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_hash_des_ffff0000907d54ce);
      puVar5 = &DAT_ffff0000907f0ad8;
      pcVar6 = s_Invalid_tag_for_hash_descriptor__ffff0000907df120;
    }
    FUN_ffff0000907c0ee8(uVar4,&DAT_ffff0000907ebfa6,puVar5,s___ERROR__ffff0000907e07dc,pcVar6,0);
  }
  return 0;
}


