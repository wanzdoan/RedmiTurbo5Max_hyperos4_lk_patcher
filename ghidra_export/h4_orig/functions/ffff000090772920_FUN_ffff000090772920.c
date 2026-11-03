// ffff000090772920  size=696  FUN_ffff000090772920

undefined8
FUN_ffff000090772920
          (long param_1,uint param_2,int param_3,uint param_4,uint param_5,uint param_6,
          char *param_7,int param_8)

{
  bool bVar1;
  uint uVar2;
  uint uVar3;
  int iVar4;
  int iVar5;
  bool bVar6;
  undefined *puVar7;
  undefined8 uVar8;
  long *plVar9;
  code *pcVar10;
  long lVar11;
  undefined4 uVar12;
  
  if (param_7 == (char *)0x0) {
    return 0xfffffffa;
  }
  if (param_8 != 0x70) {
    return 0xfffffffa;
  }
  if (*param_7 != '1') {
    return 0xfffffffa;
  }
  if (param_1 == 0) {
LAB_ffff0000907729e0:
    uVar8 = 0xfffffffe;
  }
  else {
    pcVar10 = *(code **)(param_1 + 0x40);
    *(undefined8 *)(param_1 + 0x30) = 0;
    if (pcVar10 == (code *)0x0) {
      pcVar10 = FUN_ffff00009077aa48;
      *(undefined8 *)(param_1 + 0x50) = 0;
      *(code **)(param_1 + 0x40) = FUN_ffff00009077aa48;
      lVar11 = *(long *)(param_1 + 0x48);
    }
    else {
      lVar11 = *(long *)(param_1 + 0x48);
    }
    if (lVar11 == 0) {
      *(code **)(param_1 + 0x48) = FUN_ffff00009077aa58;
    }
    uVar2 = 6;
    if (param_2 != 0xffffffff) {
      uVar2 = param_2;
    }
    if ((int)param_4 < 0) {
      if (param_4 < 0xfffffff1) goto LAB_ffff0000907729e0;
      uVar12 = 0;
      param_4 = -param_4;
      bVar6 = true;
    }
    else if (param_4 < 0x10) {
      bVar6 = false;
      uVar12 = 1;
    }
    else {
      uVar12 = 2;
      bVar6 = true;
      param_4 = param_4 - 0x10;
    }
    uVar8 = 0xfffffffe;
    if (((((param_6 < 5) && (uVar2 < 10)) && (param_3 == 8)) &&
        ((0xfffffff6 < param_5 - 10 && (7 < param_4)))) && (param_4 < 0x10)) {
      bVar1 = false;
      if (param_4 == 8) {
        bVar1 = bVar6;
      }
      if (!bVar1) {
        plVar9 = (long *)(*pcVar10)(*(undefined8 *)(param_1 + 0x50),1,0x1740);
        if (plVar9 != (long *)0x0) {
          uVar3 = 9;
          if (param_4 != 8) {
            uVar3 = param_4;
          }
          iVar4 = 1 << (ulong)(uVar3 & 0x1f);
          *(undefined4 *)(plVar9 + 1) = 0x2a;
          *(long **)(param_1 + 0x38) = plVar9;
          *(int *)(plVar9 + 10) = iVar4;
          *(uint *)((long)plVar9 + 0x54) = uVar3;
          iVar5 = 0x80 << (ulong)(param_5 & 0x1f);
          *plVar9 = param_1;
          pcVar10 = *(code **)(param_1 + 0x40);
          *(undefined4 *)(plVar9 + 6) = uVar12;
          plVar9[7] = 0;
          *(int *)(plVar9 + 0xb) = iVar4 + -1;
          *(int *)((long)plVar9 + 0x84) = iVar5;
          *(uint *)(plVar9 + 0x11) = param_5 + 7;
          uVar8 = *(undefined8 *)(param_1 + 0x50);
          *(int *)((long)plVar9 + 0x8c) = iVar5 + -1;
          *(uint *)(plVar9 + 0x12) = (param_5 + 9 & 0xff) / 3;
          lVar11 = (*pcVar10)(uVar8,iVar4,2);
          uVar8 = *(undefined8 *)(param_1 + 0x50);
          plVar9[0xc] = lVar11;
          lVar11 = (**(code **)(param_1 + 0x40))(uVar8,(int)plVar9[10],2);
          uVar8 = *(undefined8 *)(param_1 + 0x50);
          plVar9[0xe] = lVar11;
          lVar11 = (**(code **)(param_1 + 0x40))(uVar8,*(undefined4 *)((long)plVar9 + 0x84),2);
          pcVar10 = *(code **)(param_1 + 0x40);
          iVar4 = 0x40 << (ulong)(param_5 & 0x1f);
          uVar8 = *(undefined8 *)(param_1 + 0x50);
          plVar9[0xf] = lVar11;
          plVar9[0x2e7] = 0;
          *(int *)(plVar9 + 0x2e1) = iVar4;
          lVar11 = (*pcVar10)(uVar8,iVar4,4);
          uVar3 = *(uint *)(plVar9 + 0x2e1);
          plVar9[2] = lVar11;
          plVar9[3] = (ulong)uVar3 << 2;
          puVar7 = PTR_PTR_ffff000090839a08;
          if (((plVar9[0xc] != 0) && (plVar9[0xe] != 0)) && ((plVar9[0xf] != 0 && (lVar11 != 0)))) {
            *(uint *)((long)plVar9 + 0xc4) = uVar2;
            *(uint *)(plVar9 + 0x19) = param_6;
            plVar9[0x2e0] = lVar11 + (ulong)uVar3;
            *(uint *)(plVar9 + 0x2e2) = uVar3 * 3 + -3;
            *(undefined1 *)(plVar9 + 9) = 8;
            uVar8 = FUN_ffff000090772ce8(param_1);
            return uVar8;
          }
          *(undefined4 *)(plVar9 + 1) = 0x29a;
          *(undefined8 *)(param_1 + 0x30) = *(undefined8 *)(puVar7 + 0x30);
          FUN_ffff000090772bd8(param_1);
        }
        uVar8 = 0xfffffffc;
      }
    }
  }
  return uVar8;
}


