
void FUN_ffff0000be060eb8(long param_1,long param_2,ulong param_3)

{
  char cVar1;
  long lVar2;
  ulong uVar4;
  undefined1 *puVar5;
  undefined1 *puVar6;
  ulong uVar7;
  ulong uVar8;
  ulong uVar3;
  
  uVar4 = 0;
  do {
    uVar3 = param_3 + 1;
    if (param_3 == uVar4) break;
    cVar1 = *(char *)(param_2 + uVar4);
    uVar3 = uVar4 + 1;
    *(char *)(param_1 + uVar4) = cVar1;
    uVar4 = uVar3;
  } while (cVar1 != '\0');
  puVar5 = (undefined1 *)(param_1 + uVar4);
  if (uVar3 < param_3) {
    uVar4 = param_3 - uVar3;
    if (1 < uVar4) {
      uVar7 = uVar4 & 0xfffffffffffffffe;
      uVar3 = uVar3 + uVar7;
      puVar6 = puVar5 + 1;
      uVar8 = uVar7;
      do {
        *(undefined2 *)(puVar6 + -1) = 0;
        puVar6 = puVar6 + 2;
        uVar8 = uVar8 - 2;
      } while (uVar8 != 0);
      puVar5 = puVar5 + uVar7;
      if (uVar4 == uVar7) {
        return;
      }
    }
    lVar2 = param_3 - uVar3;
    do {
      lVar2 = lVar2 + -1;
      *puVar5 = 0;
      puVar5 = puVar5 + 1;
    } while (lVar2 != 0);
  }
  return;
}


