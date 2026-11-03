
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

long FUN_ffff0000b8009fb4(void)

{
  ulong uVar1;
  ulong uVar2;
  ulong uVar3;
  ulong uVar4;
  ulong uVar5;
  
  uVar2 = cntpct_el0;
  uVar3 = uVar2 & 0xffffffff;
  uVar5 = (uVar2 >> 0x20) * (ulong)DAT_ffff0000b810ff20;
  uVar4 = (_DAT_ffff0000b810ff18 >> 0x20) * uVar3;
  uVar1 = (uVar5 & 0xffffffff) + (uVar4 & 0xffffffff) + (uVar3 * DAT_ffff0000b810ff20 >> 0x20);
  return (uVar4 >> 0x20) + (_DAT_ffff0000b810ff18 & 0xffffffff) * uVar3 +
         (_DAT_ffff0000b810ff18 >> 0x20 | _DAT_ffff0000b810ff18 << 0x20) * (uVar2 >> 0x20) +
         (uVar5 >> 0x20) + (uVar1 >> 0x20) + (uVar1 >> 0x1f & 1);
}


