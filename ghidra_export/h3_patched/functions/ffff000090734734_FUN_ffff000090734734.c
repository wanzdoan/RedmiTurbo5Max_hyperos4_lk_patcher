// ffff000090734734  size=108  FUN_ffff000090734734

int FUN_ffff000090734734(long param_1,long param_2,undefined8 param_3)

{
  int iVar1;
  
  iVar1 = FUN_ffff00009077bdf4
                    (param_1,param_3,*(long *)(param_1 + 0x28) * *(long *)(param_2 + 0x48),
                     *(int *)(param_2 + 0x54) * *(int *)(param_2 + 0x50));
  if (iVar1 < 0) {
    FUN_ffff00009077de54
              (s__s__d___PART_UTILS__read_entry_f_ffff0000907d2c44,s_get_gpt_entry_ffff0000907f8111,
               0x60);
  }
  else {
    iVar1 = 0;
  }
  return iVar1;
}


