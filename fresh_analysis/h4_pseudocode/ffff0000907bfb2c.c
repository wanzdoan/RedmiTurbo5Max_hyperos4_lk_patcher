
undefined8 FUN_ffff0000907bfb2c(long param_1,undefined8 param_2)

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
  uVar2 = thunk_FUN_ffff00009077f250(s_system_ffff0000907edad2);
  uVar3 = thunk_FUN_ffff00009077f250(param_2);
  uVar4 = FUN_ffff0000907c1384(&local_50,0x20,s_system_ffff0000907edad2,uVar2,param_2,uVar3);
  if ((uVar4 & 1) == 0) {
    uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
    puVar5 = &DAT_ffff0000907e23d6;
    pcVar6 = s_System_partition_name_and_suffix_ffff0000907f91ae;
  }
  else {
    iVar1 = (**(code **)(param_1 + 0x50))(param_1,&local_50,&local_78,0x25);
    if (iVar1 == 0) {
      return 1;
    }
    if (iVar1 == 3) {
      return 0;
    }
    uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
    puVar5 = &DAT_ffff0000907ed8c0;
    pcVar6 = s_Error_getting_unique_GUID_for_sy_ffff0000907f0b07;
  }
  FUN_ffff0000907c0ee8(uVar2,&DAT_ffff0000907ebfa6,puVar5,s___ERROR__ffff0000907e07dc,pcVar6,0);
  return 0;
}


