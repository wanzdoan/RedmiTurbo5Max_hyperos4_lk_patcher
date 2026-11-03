
undefined8 FUN_ffff000090736cb4(long *param_1)

{
  uint uVar1;
  char cVar2;
  bool bVar3;
  undefined8 *puVar4;
  int iVar5;
  uint *puVar6;
  long *plVar7;
  long *plVar8;
  long lVar9;
  ulong uVar10;
  long *plVar11;
  
  puVar4 = DAT_ffff00009086dbf0;
  if (((param_1 != (long *)0x0) && (lVar9 = *param_1, lVar9 != 0)) &&
     (puVar6 = (uint *)param_1[1], puVar6 != (uint *)0x0)) {
    uVar1 = *(uint *)(param_1 + 2);
    uVar10 = (ulong)uVar1;
    if ((-1 < (int)uVar1) && (((param_1[5] != 0 && (param_1[4] != 0)) || (param_1[6] != 0)))) {
      if (uVar1 != 0) {
        do {
          if (0x18 < *puVar6) {
            return 0xfffffff8;
          }
          if (param_1[6] == 0) {
            if (puVar6[3] == 0) {
              return 0xfffffff8;
            }
            if (0x1f < (byte)puVar6[0xc]) {
              return 0xfffffff8;
            }
          }
          uVar10 = uVar10 - 1;
          puVar6 = puVar6 + 0xf;
        } while (uVar10 != 0);
      }
      plVar11 = (long *)&DAT_ffff00009086dbf0;
      if (DAT_ffff00009086dbf0 != (undefined8 *)0x0) {
        iVar5 = FUN_ffff00009077f0ac(*DAT_ffff00009086dbf0,lVar9);
        if (iVar5 == 0) {
          return 0xfffffff8;
        }
        plVar11 = (long *)0x0;
      }
      plVar7 = (long *)&DAT_ffff00009086dc00;
      if (DAT_ffff00009086dc00 != (undefined8 *)0x0) {
        iVar5 = FUN_ffff00009077f0ac(*DAT_ffff00009086dc00,lVar9);
        if (iVar5 == 0) {
          return 0xfffffff8;
        }
        plVar7 = (long *)0x0;
      }
      plVar8 = (long *)&DAT_ffff00009086dc10;
      if (puVar4 != (undefined8 *)0x0) {
        plVar11 = plVar7;
      }
      if (DAT_ffff00009086dc10 != (undefined8 *)0x0) {
        iVar5 = FUN_ffff00009077f0ac(*DAT_ffff00009086dc10,lVar9);
        if (iVar5 == 0) {
          return 0xfffffff8;
        }
        plVar8 = (long *)0x0;
      }
      plVar7 = (long *)&DAT_ffff00009086dc20;
      if (plVar11 != (long *)0x0) {
        plVar8 = plVar11;
      }
      if (DAT_ffff00009086dc20 != (undefined8 *)0x0) {
        iVar5 = FUN_ffff00009077f0ac(*DAT_ffff00009086dc20,lVar9);
        if (iVar5 == 0) {
          return 0xfffffff8;
        }
        plVar7 = (long *)0x0;
      }
      plVar11 = (long *)&DAT_ffff00009086dc30;
      if (plVar8 != (long *)0x0) {
        plVar7 = plVar8;
      }
      if (DAT_ffff00009086dc30 != (undefined8 *)0x0) {
        iVar5 = FUN_ffff00009077f0ac(*DAT_ffff00009086dc30,lVar9);
        if (iVar5 == 0) {
          return 0xfffffff8;
        }
        plVar11 = (long *)0x0;
      }
      plVar8 = (long *)&DAT_ffff00009086dc40;
      if (plVar7 != (long *)0x0) {
        plVar11 = plVar7;
      }
      if (DAT_ffff00009086dc40 != (undefined8 *)0x0) {
        iVar5 = FUN_ffff00009077f0ac(*DAT_ffff00009086dc40,lVar9);
        if (iVar5 == 0) {
          return 0xfffffff8;
        }
        plVar8 = (long *)0x0;
      }
      plVar7 = (long *)&DAT_ffff00009086dc50;
      if (plVar11 != (long *)0x0) {
        plVar8 = plVar11;
      }
      if (DAT_ffff00009086dc50 != (undefined8 *)0x0) {
        iVar5 = FUN_ffff00009077f0ac(*DAT_ffff00009086dc50,lVar9);
        if (iVar5 == 0) {
          return 0xfffffff8;
        }
        plVar7 = (long *)0x0;
      }
      plVar11 = (long *)&DAT_ffff00009086dc60;
      if (plVar8 != (long *)0x0) {
        plVar7 = plVar8;
      }
      if (DAT_ffff00009086dc60 != (undefined8 *)0x0) {
        iVar5 = FUN_ffff00009077f0ac(*DAT_ffff00009086dc60,lVar9);
        if (iVar5 == 0) {
          return 0xfffffff8;
        }
        plVar11 = (long *)0x0;
      }
      plVar8 = (long *)&DAT_ffff00009086dc70;
      if (plVar7 != (long *)0x0) {
        plVar11 = plVar7;
      }
      if (DAT_ffff00009086dc70 != (undefined8 *)0x0) {
        iVar5 = FUN_ffff00009077f0ac(*DAT_ffff00009086dc70,lVar9);
        if (iVar5 == 0) {
          return 0xfffffff8;
        }
        plVar8 = (long *)0x0;
      }
      plVar7 = (long *)&DAT_ffff00009086dc80;
      if (plVar11 != (long *)0x0) {
        plVar8 = plVar11;
      }
      if (DAT_ffff00009086dc80 != (undefined8 *)0x0) {
        iVar5 = FUN_ffff00009077f0ac(*DAT_ffff00009086dc80,lVar9);
        if (iVar5 == 0) {
          return 0xfffffff8;
        }
        plVar7 = (long *)0x0;
      }
      if (plVar8 != (long *)0x0) {
        plVar7 = plVar8;
      }
      if (plVar7 != (long *)0x0) {
        *plVar7 = (long)param_1;
        if (uVar1 == 0) {
          return 0;
        }
        lVar9 = 0;
        puVar6 = (uint *)(plVar7 + 1);
        while (uVar1 = *(uint *)(param_1[1] + lVar9 * 0x3c), (int)uVar1 < 0x20) {
          do {
            cVar2 = '\x01';
            bVar3 = (bool)ExclusiveMonitorPass(puVar6,0x10);
            if (bVar3) {
              *puVar6 = *puVar6 | (uint)(1L << ((ulong)uVar1 & 0x3f));
              cVar2 = ExclusiveMonitorsStatus();
            }
          } while (cVar2 != '\0');
          lVar9 = lVar9 + 1;
          if ((int)param_1[2] <= lVar9) {
            return 0;
          }
        }
      }
    }
  }
  return 0xfffffff8;
}


