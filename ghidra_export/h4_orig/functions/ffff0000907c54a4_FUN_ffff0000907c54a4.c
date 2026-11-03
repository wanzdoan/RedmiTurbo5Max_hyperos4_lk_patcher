// ffff0000907c54a4  size=460  FUN_ffff0000907c54a4

undefined8 FUN_ffff0000907c54a4(undefined8 param_1,long param_2,ulong param_3,long param_4)

{
  undefined4 *puVar1;
  uint uVar2;
  long lVar3;
  int iVar4;
  undefined4 uVar5;
  undefined8 uVar6;
  ulong uVar7;
  long lVar8;
  long *plVar9;
  uint uVar10;
  long lVar11;
  
  uVar6 = 2;
  if ((param_2 != 0) && (param_4 != 0)) {
    uVar7 = FUN_ffff0000907c62d8(2);
    if (((uVar7 & 1) == 0) || (lVar8 = FUN_ffff0000907c62ac(), lVar8 == 0)) {
LAB_ffff0000907c564c:
      uVar6 = 2;
    }
    else {
      lVar11 = 0;
      plVar9 = (long *)(lVar8 + 0x18);
      uVar10 = 8;
      do {
        lVar3 = plVar9[-3];
        if ((int)lVar3 == -0x6f86f7fc) {
          uVar2 = *(uint *)((long)plVar9 + -0x14);
          uVar7 = FUN_ffff00009077f250(param_2);
          if (uVar7 != uVar2) goto LAB_ffff0000907c5508;
          iVar4 = thunk_FUN_ffff00009077eae8(plVar9[-1],param_2,(ulong)uVar2);
          uVar2 = uVar10;
          if (iVar4 == 0) {
            if (*(uint *)(plVar9 + -2) < param_3) {
              lVar8 = FUN_ffff0000907c1544(param_3);
              if (lVar8 == 0) goto LAB_ffff0000907c560c;
              thunk_FUN_ffff00009077eb20(lVar8,param_4,param_3);
              *plVar9 = lVar8;
              goto LAB_ffff0000907c5640;
            }
            if (param_3 == 0) {
              *(undefined4 *)(plVar9 + -3) = 0;
            }
            thunk_FUN_ffff00009077ee50(*plVar9,0);
            if (*plVar9 == 0) goto LAB_ffff0000907c564c;
            thunk_FUN_ffff00009077eb20(*plVar9,param_4,param_3);
            *(int *)(plVar9 + -2) = (int)param_3;
            goto LAB_ffff0000907c5640;
          }
        }
        else {
LAB_ffff0000907c5508:
          uVar2 = (uint)lVar11;
          if (uVar10 != 8 || (int)lVar3 == -0x6f86f7fc) {
            uVar2 = uVar10;
          }
        }
        uVar10 = uVar2;
        lVar11 = lVar11 + 1;
        plVar9 = plVar9 + 4;
      } while (lVar11 != 8);
      if (uVar10 < 8) {
        FUN_ffff00009077e11c(s__AVB__create_a_new_persist_item__ffff0000907f0b88);
        puVar1 = (undefined4 *)(lVar8 + (ulong)uVar10 * 0x20);
        *puVar1 = 0x90790804;
        uVar5 = FUN_ffff00009077f250(param_2);
        puVar1[1] = uVar5;
        puVar1[2] = (int)param_3;
        FUN_ffff00009077f250(param_2);
        lVar11 = FUN_ffff0000907c1544();
        if (lVar11 != 0) {
          uVar6 = FUN_ffff00009077f250(param_2);
          thunk_FUN_ffff00009077eb20(lVar11,param_2,uVar6);
          *(long *)(puVar1 + 4) = lVar11;
          lVar11 = FUN_ffff0000907c1544(param_3);
          if (lVar11 != 0) {
            thunk_FUN_ffff00009077eb20(lVar11,param_4,param_3);
            *(long *)(lVar8 + (ulong)uVar10 * 0x20 + 0x18) = lVar11;
LAB_ffff0000907c5640:
            FUN_ffff0000907c62c0();
            return 0;
          }
        }
      }
LAB_ffff0000907c560c:
      uVar6 = 1;
    }
  }
  return uVar6;
}


