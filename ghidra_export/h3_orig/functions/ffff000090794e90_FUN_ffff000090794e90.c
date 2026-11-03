// ffff000090794e90  size=192  FUN_ffff000090794e90

void FUN_ffff000090794e90(long param_1)

{
  short sVar1;
  int iVar2;
  undefined8 uVar3;
  char *pcVar4;
  uint uVar5;
  
  if (DAT_ffff0000908367bc != 0) {
    sVar1 = *(short *)(param_1 + 0x88);
    pcVar4 = s_ANY_MODEL_ffff0000908367aa;
    uVar5 = DAT_ffff0000908367bc;
    do {
      if (*(short *)(pcVar4 + -2) == -1 || *(short *)(pcVar4 + -2) == sVar1) {
        uVar3 = FUN_ffff00009077ef88(pcVar4);
        iVar2 = FUN_ffff00009077f080(pcVar4,param_1 + 0x8f,uVar3);
        if ((iVar2 == 0) ||
           (iVar2 = FUN_ffff00009077ede4(pcVar4,s_ANY_MODEL_ffff0000907fc607), iVar2 == 0)) {
          *(uint *)(param_1 + 0x44) = *(uint *)(param_1 + 0x44) | uVar5;
        }
      }
      uVar5 = *(uint *)(pcVar4 + 0x2a);
      pcVar4 = pcVar4 + 0x18;
    } while (uVar5 != 0);
  }
  return;
}


