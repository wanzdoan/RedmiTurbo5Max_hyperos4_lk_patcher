// ffff000090703580  size=68  FUN_ffff000090703580

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090703580(undefined4 *param_1)

{
  undefined4 uVar1;
  
  uVar1 = _DAT_ffff00009083d078;
  *param_1 = _DAT_ffff00009083d074;
  param_1[1] = uVar1;
  *(ulong *)(param_1 + 2) = CONCAT44(uRamffff00009083d080,_DAT_ffff00009083d07c);
  uVar1 = _DAT_ffff00009083d07c;
  param_1[4] = _DAT_ffff00009083d084;
  FUN_ffff00009077de54
            (s___s__pg_sdn_sts2_3_is__d_ffff0000907cf004,s_get_poweroff_reason_ffff0000907ed804,
             uVar1);
  return;
}


