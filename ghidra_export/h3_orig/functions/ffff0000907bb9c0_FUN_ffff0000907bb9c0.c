// ffff0000907bb9c0  size=408  FUN_ffff0000907bb9c0

undefined8 FUN_ffff0000907bb9c0(undefined8 param_1,long *param_2)

{
  undefined4 uVar1;
  ulong uVar2;
  undefined8 uVar3;
  undefined *puVar4;
  char *pcVar5;
  ulong local_28;
  
  thunk_FUN_ffff00009077e858(param_2,param_1,0x5c);
  uVar2 = FUN_ffff0000907bc774(param_1,param_2);
  if ((uVar2 & 1) != 0) {
    if (*param_2 == 4) {
      uVar1 = FUN_ffff0000907c09dc((int)param_2[2]);
      *(undefined4 *)(param_2 + 2) = uVar1;
      uVar1 = FUN_ffff0000907c09dc(*(undefined4 *)((long)param_2 + 0x14));
      *(undefined4 *)((long)param_2 + 0x14) = uVar1;
      uVar1 = FUN_ffff0000907c09dc((int)param_2[3]);
      *(undefined4 *)(param_2 + 3) = uVar1;
      if ((int)param_2[2] == 0) {
        uVar3 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_chain_pa_ffff0000907d3bf1);
        puVar4 = &DAT_ffff0000907f096a;
        pcVar5 = s_Invalid_rollback_index_location_v_ffff0000907fac25;
      }
      else {
        local_28 = 0x4c;
        uVar2 = FUN_ffff0000907c0ab8(&local_28,*(undefined4 *)((long)param_2 + 0x14));
        if (((uVar2 & 1) == 0) ||
           (uVar2 = FUN_ffff0000907c0ab8(&local_28,(int)param_2[3]), (uVar2 & 1) == 0)) {
          uVar3 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_chain_pa_ffff0000907d3bf1);
          puVar4 = &DAT_ffff0000907d3c26;
          pcVar5 = s_Overflow_while_adding_up_sizes__ffff0000907e893c;
        }
        else {
          if (local_28 <= (ulong)param_2[1]) {
            return 1;
          }
          uVar3 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_chain_pa_ffff0000907d3bf1);
          puVar4 = &DAT_ffff0000907f096d;
          pcVar5 = s_Descriptor_payload_size_overflow_ffff0000907ef055;
        }
      }
    }
    else {
      uVar3 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_chain_pa_ffff0000907d3bf1);
      puVar4 = &DAT_ffff0000907f0967;
      pcVar5 = s_Invalid_tag_for_chain_partition_d_ffff0000907d5464;
    }
    FUN_ffff0000907c0874(uVar3,&DAT_ffff0000907ebe23,puVar4,s___ERROR__ffff0000907e0739,pcVar5,0);
  }
  return 0;
}


