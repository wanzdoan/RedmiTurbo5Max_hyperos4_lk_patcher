
int FUN_ffff0000be027808(long param_1,long param_2,undefined8 param_3)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000be05e000
                    (param_1,param_3,*(long *)(param_1 + 0x28) * *(long *)(param_2 + 0x48),
                     *(int *)(param_2 + 0x54) * *(int *)(param_2 + 0x50));
  if (iVar1 < 0) {
    FUN_ffff0000be05fd98
              (s__s__d___PART_UTILS__read_entry_f_ffff0000be0a5aab,s_get_gpt_entry_ffff0000be0bf777,
               0x60);
  }
  else {
    iVar1 = 0;
  }
  return iVar1;
}


