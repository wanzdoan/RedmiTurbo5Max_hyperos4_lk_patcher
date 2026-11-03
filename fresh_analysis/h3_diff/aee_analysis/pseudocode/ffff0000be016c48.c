
undefined1 * FUN_ffff0000be016c48(undefined1 *param_1,long param_2)

{
  uint uVar1;
  int iVar2;
  uint uVar3;
  long lVar4;
  ulong uVar5;
  ulong uVar6;
  undefined1 *puVar7;
  
  uVar1 = FUN_ffff0000be060e5c(param_2);
  puVar7 = param_1;
  if ((param_1 != (undefined1 *)0x0) ||
     (puVar7 = DAT_ffff0000be108748, DAT_ffff0000be108748 != (undefined1 *)0x0)) {
    DAT_ffff0000be108748 = puVar7;
    puVar7 = DAT_ffff0000be108748;
    iVar2 = FUN_ffff0000be060e5c(DAT_ffff0000be108748);
    if (0 < iVar2) {
      lVar4 = FUN_ffff0000be060c80(param_2,*puVar7);
      if (lVar4 == 0) {
        uVar5 = 0;
      }
      else {
        uVar5 = 0;
        do {
          if (iVar2 - 1 == uVar5) {
            return (undefined1 *)0x0;
          }
          lVar4 = uVar5 + 1;
          uVar5 = uVar5 + 1;
          lVar4 = FUN_ffff0000be060c80(param_2,puVar7[lVar4]);
        } while (lVar4 != 0);
      }
      puVar7 = puVar7 + uVar5;
      DAT_ffff0000be108748 = puVar7;
    }
    if (0 < (int)uVar1) {
      uVar3 = FUN_ffff0000be060e5c(puVar7);
      uVar5 = 0;
      do {
        if (0 < (int)uVar3) {
          uVar6 = 0;
          do {
            if (puVar7[uVar6] == *(char *)(param_2 + uVar5)) {
              puVar7[uVar6] = 0;
              DAT_ffff0000be108748 = puVar7 + uVar6 + 1;
              return puVar7;
            }
            uVar6 = uVar6 + 1;
          } while (uVar3 != uVar6);
        }
        uVar5 = uVar5 + 1;
      } while (uVar5 != uVar1);
    }
  }
  return puVar7;
}


