// ffff00009075905c  size=244  FUN_ffff00009075905c

void FUN_ffff00009075905c(uint param_1)

{
  uint uVar1;
  long lVar2;
  undefined4 *puVar3;
  long lVar4;
  undefined1 auStack_440 [512];
  undefined1 auStack_240 [512];
  
  FUN_ffff00009077eb88(auStack_240,0,0x200);
  FUN_ffff00009077eb88(auStack_440,0,0x200);
  lVar4 = 0;
  puVar3 = &DAT_ffff00009081f3dc + (ulong)param_1 * 0x4b;
  do {
    if (puVar3[lVar4] == -1) {
      if (lVar4 == 0) {
        return;
      }
      break;
    }
    lVar4 = lVar4 + 1;
  } while (lVar4 != 0x4b);
  do {
    uVar1 = FUN_ffff00009077f38c(auStack_440,&DAT_ffff000090801d01,*puVar3);
    if (-1 < (int)uVar1) {
      lVar2 = FUN_ffff00009077ef88(auStack_240);
      if (lVar2 + (ulong)uVar1 < 0x200) {
        FUN_ffff00009077efa8(auStack_240,auStack_440,(ulong)uVar1);
      }
    }
    FUN_ffff00009077eb88(auStack_440,0,0x200);
    lVar4 = lVar4 + -1;
    puVar3 = puVar3 + 1;
  } while (lVar4 != 0);
  return;
}


