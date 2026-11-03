
undefined8 FUN_ffff0000907bd548(undefined8 param_1,long *param_2)

{
  undefined4 uVar1;
  ulong uVar2;
  undefined8 uVar3;
  long lVar4;
  undefined *puVar5;
  char *pcVar6;
  ulong local_28;
  
  thunk_FUN_ffff00009077eb20(param_2,param_1,0xb4);
  uVar2 = FUN_ffff0000907bcde8(param_1,param_2);
  if ((uVar2 & 1) != 0) {
    if (*param_2 == 1) {
      uVar1 = FUN_ffff0000907c1050((int)param_2[2]);
      *(undefined4 *)(param_2 + 2) = uVar1;
      uVar3 = FUN_ffff0000907c1060(*(undefined8 *)((long)param_2 + 0x14));
      *(undefined8 *)((long)param_2 + 0x14) = uVar3;
      uVar3 = FUN_ffff0000907c1060(*(undefined8 *)((long)param_2 + 0x1c));
      *(undefined8 *)((long)param_2 + 0x1c) = uVar3;
      uVar3 = FUN_ffff0000907c1060(*(undefined8 *)((long)param_2 + 0x24));
      *(undefined8 *)((long)param_2 + 0x24) = uVar3;
      uVar1 = FUN_ffff0000907c1050(*(undefined4 *)((long)param_2 + 0x2c));
      *(undefined4 *)((long)param_2 + 0x2c) = uVar1;
      uVar1 = FUN_ffff0000907c1050((int)param_2[6]);
      *(undefined4 *)(param_2 + 6) = uVar1;
      uVar1 = FUN_ffff0000907c1050(*(undefined4 *)((long)param_2 + 0x34));
      *(undefined4 *)((long)param_2 + 0x34) = uVar1;
      lVar4 = FUN_ffff0000907c1060(param_2[7]);
      param_2[7] = lVar4;
      lVar4 = FUN_ffff0000907c1060(param_2[8]);
      param_2[8] = lVar4;
      uVar1 = FUN_ffff0000907c1050((int)param_2[0xd]);
      *(undefined4 *)(param_2 + 0xd) = uVar1;
      uVar1 = FUN_ffff0000907c1050(*(undefined4 *)((long)param_2 + 0x6c));
      *(undefined4 *)((long)param_2 + 0x6c) = uVar1;
      uVar1 = FUN_ffff0000907c1050((int)param_2[0xe]);
      *(undefined4 *)(param_2 + 0xe) = uVar1;
      uVar1 = FUN_ffff0000907c1050(*(undefined4 *)((long)param_2 + 0x74));
      *(undefined4 *)((long)param_2 + 0x74) = uVar1;
      local_28 = 0xa4;
      uVar2 = FUN_ffff0000907c112c(&local_28,(int)param_2[0xd]);
      if ((((uVar2 & 1) == 0) ||
          (uVar2 = FUN_ffff0000907c112c(&local_28,*(undefined4 *)((long)param_2 + 0x6c)),
          (uVar2 & 1) == 0)) ||
         (uVar2 = FUN_ffff0000907c112c(&local_28,(int)param_2[0xe]), (uVar2 & 1) == 0)) {
        uVar3 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_hashtree_ffff0000907e0801);
        puVar5 = &DAT_ffff0000907ebccb;
        pcVar6 = s_Overflow_while_adding_up_sizes__ffff0000907e8a80;
      }
      else {
        if (local_28 <= (ulong)param_2[1]) {
          return 1;
        }
        uVar3 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_hashtree_ffff0000907e0801);
        puVar5 = &DAT_ffff0000907e8aa8;
        pcVar6 = s_Descriptor_payload_size_overflow_ffff0000907ef1c6;
      }
    }
    else {
      uVar3 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_hashtree_ffff0000907e0801);
      puVar5 = &DAT_ffff0000907f0ad8;
      pcVar6 = s_Invalid_tag_for_hashtree_descrip_ffff0000907ed866;
    }
    FUN_ffff0000907c0ee8(uVar3,&DAT_ffff0000907ebfa6,puVar5,s___ERROR__ffff0000907e07dc,pcVar6,0);
  }
  return 0;
}


