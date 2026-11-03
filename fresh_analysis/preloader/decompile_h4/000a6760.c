// File offset entry: 0xa6760

undefined8 FUN_020a6660(char *param_1)

{
  uint uVar1;
  uint uVar2;
  int iVar3;
  undefined8 uVar4;
  ulong uVar5;
  ulong uVar6;
  ulong uVar7;
  char *pcVar8;
  char *pcVar9;
  
  uVar1 = *(uint *)(param_1 + 0x200);
  uVar5 = (ulong)uVar1;
  uVar4 = 0x101013;
  if (uVar1 == uRam00000000021242ec) {
    uVar2 = *(uint *)(param_1 + 0x404);
    uVar6 = (ulong)uVar2;
    if (uVar2 == uRam00000000021244f0) {
      if (uVar1 != 0) {
        pcVar9 = (char *)0x21240ec;
        uVar7 = uVar5;
        pcVar8 = param_1;
        do {
          if (*pcVar8 != *pcVar9) {
            return 0x101016;
          }
          pcVar8 = pcVar8 + 1;
          pcVar9 = pcVar9 + 1;
          uVar7 = uVar7 - 1;
        } while (uVar7 != 0);
      }
      if (uVar2 != 0) {
        param_1 = param_1 + 0x204;
        pcVar8 = (char *)0x21242f0;
        do {
          if (*param_1 != *pcVar8) {
            return 0x101014;
          }
          param_1 = param_1 + 1;
          pcVar8 = pcVar8 + 1;
          uVar6 = uVar6 - 1;
        } while (uVar6 != 0);
      }
      iVar3 = func_0x0209c6dc();
      if (iVar3 == 0) {
        uVar4 = 0x10101a;
      }
      else {
        FUN_02091664(0x2123858,0,0x200);
        FUN_020a0dfc(0x2123858,0x21240ec,uVar5);
        uRam0000000002123a58 = uVar1;
        func_0x020a0fac(0x2123858,uVar5);
        uVar4 = 0;
      }
      return uVar4;
    }
  }
  else {
    uVar4 = 0x101015;
  }
  return uVar4;
}


