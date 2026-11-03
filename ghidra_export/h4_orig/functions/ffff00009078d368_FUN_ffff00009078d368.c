// ffff00009078d368  size=344  FUN_ffff00009078d368

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009078d368(long param_1)

{
  uint uVar1;
  char cVar2;
  undefined8 uVar3;
  uint *puVar4;
  long lVar5;
  long lVar6;
  ulong uVar7;
  
  uVar7 = (ulong)*(byte *)(param_1 + 0x40);
  cVar2 = *(char *)(param_1 + 0x41);
  uVar3 = FUN_ffff00009077b2cc(*(undefined8 *)(param_1 + 8));
  FUN_ffff000090707c70(uVar3,0x40);
  lVar6 = (uVar7 - 1) * 0x10;
  lVar5 = (uVar7 - 1) * 4;
  uVar1 = *(uint *)(param_1 + 0xc) & 0xf;
  if (cVar2 == '\0') {
    *(undefined4 *)(lVar6 + -0xffffe98fe9ec) = *(undefined4 *)(param_1 + 8);
    *(uint *)(&DAT_ffff0000167014c4 + lVar5) = uVar1;
    puVar4 = (uint *)((ulong)*(byte *)(param_1 + 0x40) * 0x10 + -0xffffe98fee00);
    *puVar4 = *puVar4 | 0x20000000;
    _DAT_ffff0000167017d8 = (uint)(0x10000L << (uVar7 & 0x3f));
    _DAT_ffff000016701400 = _DAT_ffff000016701400 | _DAT_ffff0000167017d8;
    _DAT_ffff0000167017c8 = 0x10001 << (uVar7 & 0x3f);
    _DAT_ffff00001670140c =
         _DAT_ffff00001670140c & ((uint)(1L << (uVar7 & 0x3f)) ^ 0xffffffff) | _DAT_ffff0000167017d8
    ;
    lVar5 = -0xffffe98fea00;
    uVar1 = *(uint *)(uVar7 * 0x10 + -0xffffe98fea00);
  }
  else {
    *(undefined4 *)(lVar6 + -0xffffe98feaec) = *(undefined4 *)(param_1 + 8);
    *(uint *)(&DAT_ffff000016701484 + lVar5) = uVar1;
    _DAT_ffff000016701788 = 0x10001 << (uVar7 & 0x3f);
    *(uint *)(&DAT_ffff000016701100 + (ulong)*(byte *)(param_1 + 0x40) * 0x10) =
         *(uint *)(&DAT_ffff000016701100 + (ulong)*(byte *)(param_1 + 0x40) * 0x10) | 0x20000000;
    _DAT_ffff000016701400 = _DAT_ffff000016701400 | (uint)(1L << (uVar7 & 0x3f));
    lVar5 = -0xffffe98feb00;
    uVar1 = *(uint *)(uVar7 * 0x10 + -0xffffe98feb00);
  }
  if ((uVar1 >> 0xf & 1) == 0) {
    *(undefined4 *)(uVar7 * 0x10 + lVar5) = 1;
  }
  else {
    FUN_ffff00009077e11c(s__USB__QMU___s_Active_Now__ffff0000907eb968,param_1 + 0x30);
  }
  return 0;
}


