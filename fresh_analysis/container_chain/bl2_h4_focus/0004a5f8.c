// File offset entry: 0x4a5f8

int FUN_ffff0000b804a5f8
              (long param_1,undefined *param_2,ulong param_3,undefined8 *param_4,undefined4 param_5,
              undefined8 param_6,uint param_7,undefined4 param_8)

{
  undefined *puVar1;
  long *plVar2;
  int iVar3;
  long lVar4;
  ulong uVar5;
  long *plVar6;
  ulong uVar7;
  undefined8 uVar8;
  
  puVar1 = &DAT_ffff0000b808dbfe;
  if (param_2 != (undefined *)0x0) {
    puVar1 = param_2;
  }
  if (param_1 != 0) {
    if (param_3 == 0) {
      return 0;
    }
    if ((((uint)param_6 | (uint)param_3) & 0xfff) == 0) {
      if ((param_7 & 1) == 0) {
        uVar8 = 0;
      }
      else {
        if (param_4 == (undefined8 *)0x0) {
          return -8;
        }
        uVar8 = *param_4;
      }
      FUN_ffff0000b800ad90(&DAT_ffff0000b8102080,0xffffffff);
      lVar4 = FUN_ffff0000b804a31c(param_1,puVar1,param_3,uVar8,param_5,param_7,2,param_8);
      if (lVar4 == 0) {
        iVar3 = -5;
      }
      else {
        if (param_4 != (undefined8 *)0x0) {
          *param_4 = *(undefined8 *)(lVar4 + 0x38);
        }
        if ((param_7 >> 8 & 1) == 0) {
          iVar3 = FUN_ffff0000b8008864
                            (param_1 + 0x58,*(undefined8 *)(lVar4 + 0x38),param_6,param_3 >> 0xc,
                             param_8);
          if (iVar3 < 0) {
            plVar6 = *(long **)(param_1 + 0x50);
            if (plVar6 != (long *)(param_1 + 0x48)) {
              do {
                uVar5 = plVar6[7];
                if ((uVar5 <= *(ulong *)(lVar4 + 0x38)) &&
                   (uVar7 = plVar6[8], *(ulong *)(lVar4 + 0x38) <= (uVar5 + uVar7) - 1)) {
                  plVar2 = (long *)plVar6[1];
                  *plVar2 = *plVar6;
                  *(long **)(*plVar6 + 8) = plVar2;
                  *plVar6 = 0;
                  plVar6[1] = 0;
                  FUN_ffff0000b8008ae4(param_1 + 0x58,uVar5,uVar7 >> 0xc);
                  break;
                }
                plVar6 = (long *)plVar6[1];
              } while (plVar6 != (long *)(param_1 + 0x48));
            }
            FUN_ffff0000b800ae28(&DAT_ffff0000b8102080);
            thunk_FUN_ffff0000b8082558(lVar4);
            return iVar3;
          }
        }
        iVar3 = 0;
      }
      FUN_ffff0000b800ae28(&DAT_ffff0000b8102080);
      return iVar3;
    }
  }
  return -8;
}


