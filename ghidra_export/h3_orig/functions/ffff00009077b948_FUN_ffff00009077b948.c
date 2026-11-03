// ffff00009077b948  size=544  FUN_ffff00009077b948

int FUN_ffff00009077b948
              (long param_1,undefined *param_2,long param_3,undefined8 *param_4,undefined4 param_5,
              uint param_6,undefined4 param_7)

{
  undefined *puVar1;
  long *plVar2;
  int iVar3;
  ulong uVar4;
  long lVar5;
  ulong uVar6;
  long *plVar7;
  ulong uVar8;
  undefined8 uVar9;
  undefined1 *local_70;
  long *local_68;
  undefined8 local_48;
  
  local_68 = (long *)&local_70;
  local_70 = (undefined1 *)&local_70;
  uVar6 = param_3 + 0xfffU & 0xfffffffffffff000;
  if (uVar6 == 0) {
LAB_ffff00009077b9e4:
    iVar3 = -8;
  }
  else {
    puVar1 = &DAT_ffff0000907df295;
    if (param_2 != (undefined *)0x0) {
      puVar1 = param_2;
    }
    if ((param_6 & 1) == 0) {
      uVar9 = 0;
    }
    else {
      if (param_4 == (undefined8 *)0x0) goto LAB_ffff00009077b9e4;
      uVar9 = *param_4;
    }
    uVar8 = param_3 + 0xfffU >> 0xc;
    local_48 = 0;
    uVar4 = FUN_ffff00009077ae08(uVar8 & 0xffffffff,param_5,&local_48,&local_70);
    if (uVar4 < uVar8) {
      iVar3 = -5;
    }
    else {
      FUN_ffff0000907175f4(&DAT_ffff000090832888,0xffffffff);
      lVar5 = FUN_ffff00009077b4c0(param_1,puVar1,uVar6,uVar9,param_5,param_6,2,param_7);
      if (lVar5 == 0) {
        iVar3 = -5;
      }
      else {
        uVar9 = *(undefined8 *)(lVar5 + 0x38);
        if (param_4 != (undefined8 *)0x0) {
          *param_4 = uVar9;
        }
        iVar3 = FUN_ffff000090708f9c(param_1 + 0x58,uVar9,local_48,uVar8 & 0xffffffff,param_7);
        if (-1 < iVar3) {
          if ((undefined1 **)local_68 != &local_70) {
            plVar7 = (long *)(lVar5 + 0x48);
            do {
              plVar2 = (long *)local_68[1];
              *plVar2 = *local_68;
              lVar5 = *local_68;
              *local_68 = 0;
              *(long **)(lVar5 + 8) = plVar2;
              lVar5 = *plVar7;
              local_68[1] = (long)plVar7;
              *local_68 = lVar5;
              lVar5 = *plVar7;
              *plVar7 = (long)local_68;
              *(long **)(lVar5 + 8) = local_68;
            } while ((undefined1 **)local_68 != &local_70);
          }
          FUN_ffff00009071768c(&DAT_ffff000090832888);
          return 0;
        }
        if (param_1 != 0) {
          plVar7 = *(long **)(param_1 + 0x50);
          if (plVar7 != (long *)(param_1 + 0x48)) {
            do {
              uVar6 = plVar7[7];
              if ((uVar6 <= *(ulong *)(lVar5 + 0x38)) &&
                 (uVar4 = plVar7[8], *(ulong *)(lVar5 + 0x38) <= (uVar6 + uVar4) - 1)) {
                plVar2 = (long *)plVar7[1];
                *plVar2 = *plVar7;
                *(long **)(*plVar7 + 8) = plVar2;
                *plVar7 = 0;
                plVar7[1] = 0;
                FUN_ffff00009070921c(param_1 + 0x58,uVar6,uVar4 >> 0xc);
                break;
              }
              plVar7 = (long *)plVar7[1];
            } while (plVar7 != (long *)(param_1 + 0x48));
          }
        }
        thunk_FUN_ffff0000907c2ad8(lVar5);
      }
      FUN_ffff00009071768c(&DAT_ffff000090832888);
      FUN_ffff00009077ac04(&local_70);
    }
  }
  return iVar3;
}


