
ulong * FUN_ffff0000907be638(ulong *param_1)

{
  ulong *puVar1;
  ulong uVar2;
  long lVar3;
  ulong uVar4;
  ulong uVar5;
  ulong uVar6;
  uint uVar7;
  long lVar8;
  
  uVar4 = param_1[9];
  puVar1 = param_1 + 10;
  uVar2 = param_1[8] + uVar4;
  lVar3 = 1;
  if ((~(uint)uVar4 & 0x70) == 0) {
    lVar3 = 2;
  }
  lVar8 = lVar3 * 0x80;
  thunk_FUN_ffff00009077ee50((long)puVar1 + uVar4,0,lVar8 - uVar4);
  *(undefined1 *)((long)puVar1 + param_1[9]) = 0x80;
  *(char *)((long)puVar1 + lVar8 + -2) = (char)(uVar2 >> 5);
  *(char *)((long)puVar1 + lVar8 + -3) = (char)(uVar2 >> 0xd);
  *(char *)((long)puVar1 + lVar8 + -4) = (char)(uVar2 >> 0x15);
  *(char *)((long)puVar1 + lVar8 + -5) = (char)(uVar2 >> 0x1d);
  *(char *)((long)puVar1 + lVar8 + -1) = (char)((int)uVar2 << 3);
  uVar7 = (uint)(uVar2 >> 0x20);
  *(char *)((long)puVar1 + lVar8 + -6) = (char)(uVar7 >> 5);
  *(char *)((long)puVar1 + lVar8 + -7) = (char)(uVar7 >> 0xd);
  *(char *)(puVar1 + lVar3 * 0x10 + -1) = (char)(uVar7 >> 0x15);
  FUN_ffff0000907be450(param_1,puVar1,lVar3);
  uVar2 = (*param_1 & 0xff00ff00ff00ff00) >> 8 | (*param_1 & 0xff00ff00ff00ff) << 8;
  uVar2 = (uVar2 & 0xffff0000ffff0000) >> 0x10 | (uVar2 & 0xffff0000ffff) << 0x10;
  uVar4 = (param_1[1] & 0xff00ff00ff00ff00) >> 8 | (param_1[1] & 0xff00ff00ff00ff) << 8;
  uVar4 = (uVar4 & 0xffff0000ffff0000) >> 0x10 | (uVar4 & 0xffff0000ffff) << 0x10;
  uVar5 = (param_1[2] & 0xff00ff00ff00ff00) >> 8 | (param_1[2] & 0xff00ff00ff00ff) << 8;
  uVar5 = (uVar5 & 0xffff0000ffff0000) >> 0x10 | (uVar5 & 0xffff0000ffff) << 0x10;
  uVar6 = (param_1[3] & 0xff00ff00ff00ff00) >> 8 | (param_1[3] & 0xff00ff00ff00ff) << 8;
  uVar6 = (uVar6 & 0xffff0000ffff0000) >> 0x10 | (uVar6 & 0xffff0000ffff) << 0x10;
  param_1[0x2a] = uVar2 >> 0x20 | uVar2 << 0x20;
  param_1[0x2b] = uVar4 >> 0x20 | uVar4 << 0x20;
  uVar2 = (param_1[4] & 0xff00ff00ff00ff00) >> 8 | (param_1[4] & 0xff00ff00ff00ff) << 8;
  uVar2 = (uVar2 & 0xffff0000ffff0000) >> 0x10 | (uVar2 & 0xffff0000ffff) << 0x10;
  param_1[0x2c] = uVar5 >> 0x20 | uVar5 << 0x20;
  param_1[0x2d] = uVar6 >> 0x20 | uVar6 << 0x20;
  uVar4 = (param_1[5] & 0xff00ff00ff00ff00) >> 8 | (param_1[5] & 0xff00ff00ff00ff) << 8;
  uVar4 = (uVar4 & 0xffff0000ffff0000) >> 0x10 | (uVar4 & 0xffff0000ffff) << 0x10;
  uVar5 = (param_1[6] & 0xff00ff00ff00ff00) >> 8 | (param_1[6] & 0xff00ff00ff00ff) << 8;
  uVar5 = (uVar5 & 0xffff0000ffff0000) >> 0x10 | (uVar5 & 0xffff0000ffff) << 0x10;
  uVar6 = (param_1[7] & 0xff00ff00ff00ff00) >> 8 | (param_1[7] & 0xff00ff00ff00ff) << 8;
  uVar6 = (uVar6 & 0xffff0000ffff0000) >> 0x10 | (uVar6 & 0xffff0000ffff) << 0x10;
  param_1[0x2e] = uVar2 >> 0x20 | uVar2 << 0x20;
  param_1[0x2f] = uVar4 >> 0x20 | uVar4 << 0x20;
  param_1[0x30] = uVar5 >> 0x20 | uVar5 << 0x20;
  param_1[0x31] = uVar6 >> 0x20 | uVar6 << 0x20;
  return param_1 + 0x2a;
}


