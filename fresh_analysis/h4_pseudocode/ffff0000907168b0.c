
long FUN_ffff0000907168b0(void)

{
  ulong uVar1;
  ulong uVar2;
  ulong uVar3;
  ulong uVar4;
  ulong uVar5;
  
  uVar2 = cntpct_el0;
  uVar3 = uVar2 & 0xffffffff;
  uVar5 = (uVar2 >> 0x20) * (ulong)DAT_ffff0000908436a8;
  uVar4 = (DAT_ffff0000908436a0 >> 0x20) * uVar3;
  uVar1 = (uVar5 & 0xffffffff) + (uVar4 & 0xffffffff) + (uVar3 * DAT_ffff0000908436a8 >> 0x20);
  return (uVar4 >> 0x20) + (DAT_ffff0000908436a0 & 0xffffffff) * uVar3 +
         (DAT_ffff0000908436a0 >> 0x20 | DAT_ffff0000908436a0 << 0x20) * (uVar2 >> 0x20) +
         (uVar5 >> 0x20) + (uVar1 >> 0x20) + (uVar1 >> 0x1f & 1);
}


