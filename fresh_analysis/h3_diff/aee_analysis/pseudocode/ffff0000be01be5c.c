
void FUN_ffff0000be01be5c(long param_1)

{
  long lVar1;
  char *pcVar2;
  undefined8 local_30;
  undefined8 local_28;
  
  local_30 = 0;
  local_28 = 0;
  FUN_ffff0000be0607b0(&local_30,param_1 + 8,0x10);
  if ((int)local_28 - 3U < 0xfffffffe) {
    pcVar2 = s_unknown_def_type_ffff0000be0ad525;
  }
  else if (((int)local_30 == 0) || (local_30._4_4_ == 0)) {
    pcVar2 = s_sram_addr_size_zero_ffff0000be0c05d8;
  }
  else {
    if (local_30._4_4_ < local_28._4_4_) {
      lVar1 = FUN_ffff0000be05d1c0();
      lVar1 = lVar1 + (local_28 >> 0x20);
      if (*(int *)(lVar1 + 0xc) == 0) {
        return;
      }
      if (*(int *)(lVar1 + 0x10) != 0) {
        FUN_ffff0000be01bc3c();
        DAT_ffff0000be10ccb0 = 1;
        FUN_ffff0000be05fd98
                  (s_log_store__init_log_store_addr_0_ffff0000be0a6afd,*(undefined4 *)(lVar1 + 0xc),
                   *(undefined4 *)(lVar1 + 0x10));
        DAT_ffff0000be10ccb0 = 0;
        return;
      }
      return;
    }
    pcVar2 = s_memory_offset_invalid_value_ffff0000be0b3f5f;
  }
  DAT_ffff0000be10ccb0 = 1;
  FUN_ffff0000be05fd98(pcVar2);
  DAT_ffff0000be10ccb0 = 0;
  return;
}


