// ffff000090771e38  size=488  FUN_ffff000090771e38

undefined8 FUN_ffff000090771e38(undefined8 param_1,undefined8 *param_2,int param_3)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  undefined8 *puVar5;
  long lVar6;
  undefined8 *puVar7;
  int local_64;
  uint local_48;
  uint local_44;
  
  if ((param_2 != (undefined8 *)0x0) && (*(int *)*param_2 == 0x1000000)) {
    puVar7 = param_2 + 2;
    puVar5 = (undefined8 *)*puVar7;
    if (puVar5 != (undefined8 *)0x0) {
      do {
        if (*(int *)*puVar5 == 0x3000000) {
          lVar3 = FUN_ffff000090770e3c(param_1,puVar5[2]);
          if (lVar3 == 0) {
            return 0xffffffff;
          }
          local_48 = 0;
          lVar4 = FUN_ffff000090770f18(*puVar7,&local_48);
          if (lVar4 == 0) {
            return 0xffffffff;
          }
          if ((local_48 & 3) != 0) {
            return 0xffffffff;
          }
          local_64 = 0;
          lVar3 = FUN_ffff000090770f18(lVar3,&local_64);
          if (lVar3 == 0) {
            return 0xffffffff;
          }
          if (0 < (int)local_48) {
            iVar2 = 0;
            do {
              uVar1 = (*(uint *)(lVar4 + iVar2) & 0xff00ff00) >> 8 |
                      (*(uint *)(lVar4 + iVar2) & 0xff00ff) << 8;
              lVar6 = (long)(int)(uVar1 >> 0x10 | uVar1 << 0x10);
              if ((ulong)(long)local_64 < lVar6 + 4U) {
                return 0xffffffff;
              }
              lVar6 = lVar3 + lVar6;
              local_44 = 0;
              thunk_FUN_ffff00009077eb20(&local_44,lVar6,4);
              uVar1 = (local_44 & 0xff00ff00) >> 8 | (local_44 & 0xff00ff) << 8;
              uVar1 = (uVar1 >> 0x10 | uVar1 << 0x10) + param_3;
              uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
              local_44 = uVar1 >> 0x10 | uVar1 << 0x10;
              thunk_FUN_ffff00009077eb20(lVar6,&local_44,4);
              iVar2 = iVar2 + 4;
            } while (iVar2 < (int)local_48);
          }
          puVar5 = (undefined8 *)*puVar7;
        }
        puVar7 = puVar5 + 1;
        puVar5 = (undefined8 *)*puVar7;
      } while (puVar5 != (undefined8 *)0x0);
      if (*(int *)*param_2 != 0x1000000) {
        return 0;
      }
    }
    param_2 = param_2 + 2;
    puVar5 = (undefined8 *)*param_2;
    while (puVar5 != (undefined8 *)0x0) {
      if (*(int *)*puVar5 == 0x1000000) {
        lVar3 = FUN_ffff000090771234(param_1,(int *)*puVar5 + 1);
        if ((lVar3 == 0) || (iVar2 = FUN_ffff000090771e38(lVar3,*param_2,param_3), iVar2 < 0)) {
          return 0xffffffff;
        }
        puVar5 = (undefined8 *)*param_2;
      }
      param_2 = puVar5 + 1;
      puVar5 = (undefined8 *)*param_2;
    }
  }
  return 0;
}


