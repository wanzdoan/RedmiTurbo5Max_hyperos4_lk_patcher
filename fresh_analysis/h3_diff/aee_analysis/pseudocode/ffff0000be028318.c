
undefined8 FUN_ffff0000be028318(long param_1,undefined8 *param_2)

{
  long lVar1;
  int iVar2;
  undefined8 uVar3;
  long lVar4;
  char *pcVar5;
  ulong uVar6;
  
  if (param_1 == 0) {
    uVar3 = 0xffffffea;
  }
  else {
    iVar2 = FUN_ffff0000be0283e0();
    lVar4 = DAT_ffff0000be0d6618;
    pcVar5 = DAT_ffff0000be0d6610;
    lVar1 = DAT_ffff0000be0d6608;
    if (iVar2 < 0) {
      uVar3 = 0xfffffffb;
    }
    else {
      FUN_ffff0000be01a730(&LAB_ffff0000be0d6628,0xffffffff);
      uVar6 = (ulong)*(uint *)(lVar1 + 0x50);
      if (0 < (int)*(uint *)(lVar1 + 0x50)) {
        do {
          if ((*pcVar5 == '\0') && (pcVar5[1] == '\0')) break;
          iVar2 = FUN_ffff0000be060f54(param_1,lVar4,0x48);
          if (iVar2 == 0) {
            *param_2 = pcVar5;
            break;
          }
          lVar4 = lVar4 + 0x6d;
          pcVar5 = pcVar5 + 0x80;
          uVar6 = uVar6 - 1;
        } while (uVar6 != 0);
      }
      FUN_ffff0000be01a7c8(&LAB_ffff0000be0d6628);
      uVar3 = 0;
    }
  }
  return uVar3;
}


