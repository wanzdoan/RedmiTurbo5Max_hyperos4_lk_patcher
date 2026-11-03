
void FUN_ffff0000b8058c24(long param_1)

{
  short sVar1;
  int iVar2;
  undefined8 uVar3;
  char *pcVar4;
  uint uVar5;
  
  if (DAT_ffff0000b8106428 != 0) {
    sVar1 = *(short *)(param_1 + 0x88);
    pcVar4 = s_ANY_MODEL_ffff0000b8106416;
    uVar5 = DAT_ffff0000b8106428;
    do {
      if (*(short *)(pcVar4 + -2) == -1 || *(short *)(pcVar4 + -2) == sVar1) {
        uVar3 = FUN_ffff0000b804d3cc(pcVar4);
        iVar2 = FUN_ffff0000b804d4c4(pcVar4,param_1 + 0x8f,uVar3);
        if ((iVar2 == 0) ||
           (iVar2 = FUN_ffff0000b804d248(pcVar4,s_ANY_MODEL_ffff0000b809df85), iVar2 == 0)) {
          *(uint *)(param_1 + 0x44) = *(uint *)(param_1 + 0x44) | uVar5;
        }
      }
      uVar5 = *(uint *)(pcVar4 + 0x2a);
      pcVar4 = pcVar4 + 0x18;
    } while (uVar5 != 0);
  }
  return;
}


