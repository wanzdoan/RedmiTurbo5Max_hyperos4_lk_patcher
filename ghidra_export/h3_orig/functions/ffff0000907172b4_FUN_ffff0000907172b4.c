// ffff0000907172b4  size=116  FUN_ffff0000907172b4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907172b4(undefined8 param_1)

{
  long lVar1;
  ulong uVar2;
  undefined8 uVar3;
  
  cntp_ctl_el0 = 0;
  InstructionSynchronizationBarrier();
  if (_DAT_ffff000090843638 != (code *)0x0) {
    uVar2 = cntpct_el0;
    lVar1 = (_DAT_ffff000090843658 >> 0x20) * (uVar2 & 0xffffffff) +
            (uVar2 >> 0x20) * (ulong)_DAT_ffff000090843660 +
            ((uVar2 & 0xffffffff) * (ulong)_DAT_ffff000090843660 >> 0x20);
                    /* WARNING: Could not recover jumptable at 0xffff000090717318. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
    uVar3 = (*_DAT_ffff000090843638)
                      (param_1,((int)_DAT_ffff000090843658 * (int)uVar2 +
                                (int)(_DAT_ffff000090843658 >> 0x20) * (int)(uVar2 >> 0x20) +
                               (int)((ulong)lVar1 >> 0x20)) - ((int)lVar1 >> 0x1f));
    return uVar3;
  }
  return 0;
}


