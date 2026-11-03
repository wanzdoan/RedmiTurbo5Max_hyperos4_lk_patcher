// ffff0000907bf4b8  size=284  FUN_ffff0000907bf4b8

undefined8 FUN_ffff0000907bf4b8(long param_1,undefined8 param_2)

{
  int iVar1;
  undefined8 uVar2;
  undefined8 uVar3;
  ulong uVar4;
  undefined *puVar5;
  char *pcVar6;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined8 local_68;
  undefined5 uStack_60;
  undefined3 local_5b;
  undefined5 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  uStack_58 = 0;
  local_68 = 0;
  uStack_60 = 0;
  local_5b = 0;
  local_78 = 0;
  uStack_70 = 0;
  uVar2 = thunk_FUN_ffff00009077ef88(s_system_ffff0000907ed961);
  uVar3 = thunk_FUN_ffff00009077ef88(param_2);
  uVar4 = FUN_ffff0000907c0d10(&local_50,0x20,s_system_ffff0000907ed961,uVar2,param_2,uVar3);
  if ((uVar4 & 1) == 0) {
    uVar2 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
    puVar5 = &DAT_ffff0000907e2305;
    pcVar6 = s_System_partition_name_and_suffix_ffff0000907f9012;
  }
  else {
    iVar1 = (**(code **)(param_1 + 0x50))(param_1,&local_50,&local_78,0x25);
    if (iVar1 == 0) {
      return 1;
    }
    if (iVar1 == 3) {
      return 0;
    }
    uVar2 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
    puVar5 = &DAT_ffff0000907ed73d;
    pcVar6 = s_Error_getting_unique_GUID_for_sy_ffff0000907f0996;
  }
  FUN_ffff0000907c0874(uVar2,&DAT_ffff0000907ebe23,puVar5,s___ERROR__ffff0000907e0739,pcVar6,0);
  return 0;
}


