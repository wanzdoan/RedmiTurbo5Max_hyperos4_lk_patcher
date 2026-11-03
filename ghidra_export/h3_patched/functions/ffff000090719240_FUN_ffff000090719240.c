// ffff000090719240  size=224  FUN_ffff000090719240

void FUN_ffff000090719240(long param_1)

{
  long lVar1;
  char *pcVar2;
  undefined8 local_30;
  undefined8 local_28;
  
  local_30 = 0;
  local_28 = 0;
  FUN_ffff00009077e858(&local_30,param_1 + 8,0x10);
  if ((int)local_28 - 3U < 0xfffffffe) {
    pcVar2 = s_unknown_def_type_ffff0000907dda97;
  }
  else if (((int)local_30 == 0) || (local_30._4_4_ == 0)) {
    pcVar2 = s_sram_addr_size_zero_ffff0000907f943a;
  }
  else {
    if (local_30._4_4_ < local_28._4_4_) {
      lVar1 = FUN_ffff00009077b004();
      lVar1 = lVar1 + (local_28 >> 0x20);
      if (*(int *)(lVar1 + 0xc) == 0) {
        return;
      }
      if (*(int *)(lVar1 + 0x10) != 0) {
        FUN_ffff000090719020();
        DAT_ffff000090843c30 = 1;
        FUN_ffff00009077de54
                  (s_log_store__init_log_store_addr_0_ffff0000907d3fcc,*(undefined4 *)(lVar1 + 0xc),
                   *(undefined4 *)(lVar1 + 0x10));
        DAT_ffff000090843c30 = 0;
        return;
      }
      return;
    }
    pcVar2 = s_memory_offset_invalid_value_ffff0000907e750b;
  }
  DAT_ffff000090843c30 = 1;
  FUN_ffff00009077de54(pcVar2);
  DAT_ffff000090843c30 = 0;
  return;
}


