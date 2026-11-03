// ffff000090751988  size=92  FUN_ffff000090751988

undefined8 FUN_ffff000090751988(int param_1)

{
  long lVar1;
  uint uVar2;
  long *plVar3;
  long lVar4;
  
  uVar2 = 0;
  if (param_1 != 0x2b) {
    uVar2 = (uint)(param_1 != 0x2d);
  }
  lVar1 = 1;
  if (param_1 != 0x2b) {
    lVar1 = 2;
  }
  lVar1 = lVar1 - (ulong)uVar2;
  plVar3 = (long *)(s_0123456789abcdef_ffff000090804c70 + (ulong)uVar2 * 8 + 0x10);
  do {
    lVar4 = *plVar3;
    lVar1 = lVar1 + -1;
    *(uint *)(lVar4 + 0x30) = *(uint *)(lVar4 + 0x30) | 1;
    *(uint *)(lVar4 + 0x30) = *(uint *)(lVar4 + 0x30) & 0xfffffffe;
    plVar3 = plVar3 + 1;
  } while (lVar1 != 0);
  return 0;
}


