// ffff000090723830  size=100  FUN_ffff000090723830

void FUN_ffff000090723830(code *param_1)

{
  ulong uVar1;
  int local_24;
  undefined8 local_8;
  
  local_8 = 0;
  local_24 = 0;
  FUN_ffff000090781968(&local_8,&local_24);
  uVar1 = (*param_1)(local_8,(long)local_24);
  if ((uVar1 & 1) == 0) {
    FUN_ffff00009077de54
              (s__s__failed_to_set_dram_data__ffff0000907ef4ec,s_save_dram_data_ffff0000907da7cf);
  }
  return;
}


