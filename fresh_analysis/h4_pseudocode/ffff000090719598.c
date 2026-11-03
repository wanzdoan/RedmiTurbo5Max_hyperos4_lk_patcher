
void FUN_ffff000090719598(undefined8 param_1,undefined8 param_2,ulong param_3)

{
  int iVar1;
  uint uVar2;
  ulong uVar3;
  long lVar4;
  uint uVar5;
  
  if ((param_3 != 0) && ((DAT_ffff000090843c70 & 1) == 0)) {
    if ((DAT_ffff000090843c78 == 0) && (DAT_ffff000090843c80 != -1)) {
      if (DAT_ffff000090843c80 != 0x5000) {
        lVar4 = (long)DAT_ffff000090843c80;
        uVar3 = 0x5000 - lVar4;
        if (lVar4 + param_3 < 0x5001) {
          uVar3 = param_3;
        }
        FUN_ffff00009077eb20(&DAT_ffff000090843c84 + lVar4,param_2,uVar3);
        DAT_ffff000090843c80 = DAT_ffff000090843c80 + (int)uVar3;
      }
    }
    else if ((((*(uint *)(DAT_ffff000090843c68 + 0x10) >> 9 & 1) != 0) &&
             ((*(uint *)(DAT_ffff000090843c68 + 0x10) >> 4 & 1) == 0)) &&
            (DAT_ffff000090843c78 != 0)) {
      if (DAT_ffff000090843c80 < 1) {
        uVar5 = *(uint *)(DAT_ffff000090843c68 + 0x58);
        lVar4 = DAT_ffff000090843c68;
      }
      else {
        iVar1 = *(uint *)(DAT_ffff000090843c68 + 0x58) + *(uint *)(DAT_ffff000090843c68 + 0x54);
        if (*(uint *)(DAT_ffff000090843c68 + 0x1c) < (uint)(iVar1 + DAT_ffff000090843c80)) {
          DAT_ffff000090843c80 = *(uint *)(DAT_ffff000090843c68 + 0x1c) - iVar1;
        }
        FUN_ffff00009077eb20
                  (DAT_ffff000090843c78 + (ulong)*(uint *)(DAT_ffff000090843c68 + 0x54) +
                   (ulong)*(uint *)(DAT_ffff000090843c68 + 0x58),&DAT_ffff000090843c84,
                   (long)DAT_ffff000090843c80);
        lVar4 = DAT_ffff000090843c68;
        uVar5 = *(int *)(DAT_ffff000090843c68 + 0x58) + DAT_ffff000090843c80;
        *(uint *)(DAT_ffff000090843c68 + 0x58) = uVar5;
        DAT_ffff000090843c80 = -1;
      }
      uVar2 = uVar5 + *(uint *)(lVar4 + 0x54);
      if ((ulong)*(uint *)(lVar4 + 0x1c) < uVar2 + param_3) {
        param_3 = (ulong)(*(uint *)(lVar4 + 0x1c) - uVar2);
        *(uint *)(lVar4 + 0x10) = *(uint *)(lVar4 + 0x10) | 0x10;
      }
      FUN_ffff00009077eb20
                (DAT_ffff000090843c78 + (ulong)*(uint *)(lVar4 + 0x54) + (ulong)uVar5,param_2,
                 param_3);
      *(int *)(DAT_ffff000090843c68 + 0x58) = *(int *)(DAT_ffff000090843c68 + 0x58) + (int)param_3;
    }
  }
  return;
}


