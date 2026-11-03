
void FUN_ffff0000be072948(long param_1)

{
  short sVar1;
  int iVar2;
  undefined8 uVar3;
  char *pcVar4;
  uint uVar5;
  
  if (DAT_ffff0000be0f2dd4 != 0) {
    sVar1 = *(short *)(param_1 + 0x88);
    pcVar4 = s_ANY_MODEL_ffff0000be0f2dc2;
    uVar5 = DAT_ffff0000be0f2dd4;
    do {
      if (*(short *)(pcVar4 + -2) == -1 || *(short *)(pcVar4 + -2) == sVar1) {
        uVar3 = FUN_ffff0000be060e5c(pcVar4);
        iVar2 = FUN_ffff0000be060f54(pcVar4,param_1 + 0x8f,uVar3);
        if ((iVar2 == 0) ||
           (iVar2 = FUN_ffff0000be060cb8(pcVar4,s_ANY_MODEL_ffff0000be0c25b2), iVar2 == 0)) {
          *(uint *)(param_1 + 0x44) = *(uint *)(param_1 + 0x44) | uVar5;
        }
      }
      uVar5 = *(uint *)(pcVar4 + 0x2a);
      pcVar4 = pcVar4 + 0x18;
    } while (uVar5 != 0);
  }
  return;
}


