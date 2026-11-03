// ffff0000907034f8  size=68  FUN_ffff0000907034f8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907034f8(undefined4 *param_1)

{
  undefined4 uVar1;
  
  uVar1 = _DAT_ffff00009083d01c;
  *param_1 = _DAT_ffff00009083d018;
  param_1[1] = uVar1;
  *(ulong *)(param_1 + 2) = CONCAT44(uRamffff00009083d024,_DAT_ffff00009083d020);
  uVar1 = _DAT_ffff00009083d020;
  param_1[4] = _DAT_ffff00009083d028;
  FUN_ffff00009077e11c
            (s___s__pg_sdn_sts2_3_is__d_ffff0000907cf004,s_get_poweroff_reason_ffff0000907ed987,
             uVar1);
  return;
}


