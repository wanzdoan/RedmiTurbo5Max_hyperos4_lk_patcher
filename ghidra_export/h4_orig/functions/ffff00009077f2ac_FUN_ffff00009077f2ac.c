// ffff00009077f2ac  size=56  FUN_ffff00009077f2ac

void FUN_ffff00009077f2ac(long param_1,long param_2,ulong param_3)

{
  char cVar1;
  long lVar2;
  undefined1 *puVar4;
  ulong uVar5;
  undefined1 *puVar6;
  ulong uVar7;
  ulong uVar8;
  ulong uVar3;
  
  uVar3 = param_3 + 1;
  uVar5 = 0;
  while (param_3 != uVar5) {
    cVar1 = *(char *)(param_2 + uVar5);
    *(char *)(param_1 + uVar5) = cVar1;
    uVar5 = uVar5 + 1;
    if (cVar1 == '\0') {
      FUN_ffff00009077f2e4();
      return;
    }
  }
  puVar4 = (undefined1 *)(param_1 + uVar5);
  if (uVar3 < param_3) {
    uVar5 = param_3 - uVar3;
    if (1 < uVar5) {
      uVar7 = uVar5 & 0xfffffffffffffffe;
      uVar3 = uVar3 + uVar7;
      puVar6 = puVar4 + 1;
      uVar8 = uVar7;
      do {
        *(undefined2 *)(puVar6 + -1) = 0;
        puVar6 = puVar6 + 2;
        uVar8 = uVar8 - 2;
      } while (uVar8 != 0);
      puVar4 = puVar4 + uVar7;
      if (uVar5 == uVar7) {
        return;
      }
    }
    lVar2 = param_3 - uVar3;
    do {
      lVar2 = lVar2 + -1;
      *puVar4 = 0;
      puVar4 = puVar4 + 1;
    } while (lVar2 != 0);
  }
  return;
}


