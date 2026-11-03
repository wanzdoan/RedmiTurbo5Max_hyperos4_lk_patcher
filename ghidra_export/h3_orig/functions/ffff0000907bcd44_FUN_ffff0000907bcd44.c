// ffff0000907bcd44  size=400  FUN_ffff0000907bcd44

undefined8 FUN_ffff0000907bcd44(undefined8 param_1,long *param_2)

{
  undefined4 uVar1;
  ulong uVar2;
  long lVar3;
  undefined8 uVar4;
  undefined *puVar5;
  char *pcVar6;
  ulong local_28;
  
  thunk_FUN_ffff00009077e858(param_2,param_1,0x84);
  uVar2 = FUN_ffff0000907bc774(param_1,param_2);
  if ((uVar2 & 1) != 0) {
    if (*param_2 == 2) {
      lVar3 = FUN_ffff0000907c09ec(param_2[2]);
      param_2[2] = lVar3;
      uVar1 = FUN_ffff0000907c09dc((int)param_2[7]);
      *(undefined4 *)(param_2 + 7) = uVar1;
      uVar1 = FUN_ffff0000907c09dc(*(undefined4 *)((long)param_2 + 0x3c));
      *(undefined4 *)((long)param_2 + 0x3c) = uVar1;
      uVar1 = FUN_ffff0000907c09dc((int)param_2[8]);
      *(undefined4 *)(param_2 + 8) = uVar1;
      uVar1 = FUN_ffff0000907c09dc(*(undefined4 *)((long)param_2 + 0x44));
      *(undefined4 *)((long)param_2 + 0x44) = uVar1;
      local_28 = 0x74;
      uVar2 = FUN_ffff0000907c0ab8(&local_28,(int)param_2[7]);
      if ((((uVar2 & 1) == 0) ||
          (uVar2 = FUN_ffff0000907c0ab8(&local_28,*(undefined4 *)((long)param_2 + 0x3c)),
          (uVar2 & 1) == 0)) ||
         (uVar2 = FUN_ffff0000907c0ab8(&local_28,(int)param_2[8]), (uVar2 & 1) == 0)) {
        uVar4 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_hash_des_ffff0000907d5491);
        puVar5 = &DAT_ffff0000907ea313;
        pcVar6 = s_Overflow_while_adding_up_sizes__ffff0000907e893c;
      }
      else {
        if (local_28 <= (ulong)param_2[1]) {
          return 1;
        }
        uVar4 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_hash_des_ffff0000907d5491);
        puVar5 = &DAT_ffff0000907f3b85;
        pcVar6 = s_Descriptor_payload_size_overflow_ffff0000907ef055;
      }
    }
    else {
      uVar4 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_hash_des_ffff0000907d5491);
      puVar5 = &DAT_ffff0000907f0967;
      pcVar6 = s_Invalid_tag_for_hash_descriptor__ffff0000907df07d;
    }
    FUN_ffff0000907c0874(uVar4,&DAT_ffff0000907ebe23,puVar5,s___ERROR__ffff0000907e0739,pcVar6,0);
  }
  return 0;
}


