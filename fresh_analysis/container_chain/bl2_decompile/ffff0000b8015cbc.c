
undefined8 FUN_ffff0000b8015cbc(long *param_1,int *param_2)

{
  long lVar1;
  undefined8 uVar2;
  ulong uVar3;
  long lVar4;
  int iVar5;
  long *plVar6;
  long *plVar7;
  long lVar8;
  int iVar9;
  ulong uVar10;
  undefined4 local_44;
  
  *param_2 = 0;
  lVar1 = FUN_ffff0000b804bccc(0x1000);
  *param_1 = lVar1;
  lVar4 = DAT_ffff0000b811b0b8;
  uVar2 = 0;
  if (lVar1 != 0) {
    if (DAT_ffff0000b811b0b8 == 0) {
      uVar2 = 0;
    }
    else {
      if (*(int *)(DAT_ffff0000b811b0b8 + 8) != 0) {
        uVar3 = 0;
        plVar6 = (long *)(*(long *)(DAT_ffff0000b811b0b8 + 0x30) + 8);
        do {
          uVar3 = uVar3 + 1;
          *(uint *)(plVar6[-1] + 0x888) = *(uint *)(plVar6[-1] + 0x888) | 2;
          plVar7 = (long *)*plVar6;
          lVar8 = *plVar7;
          *(uint *)(lVar8 + 0x888) = *(uint *)(lVar8 + 0x888) | 2;
          lVar8 = plVar7[1];
          *(uint *)(lVar8 + 0x888) = *(uint *)(lVar8 + 0x888) | 2;
          plVar6 = plVar6 + 2;
        } while (uVar3 < *(uint *)(lVar4 + 8));
      }
      iVar5 = *param_2;
      local_44 = 0;
      if ((long)iVar5 - 0xfd0U < 0xfffffffffffff000) {
LAB_ffff0000b8015eac:
        uVar2 = 0xffffffff;
      }
      else {
        FUN_ffff0000b804cd40(lVar1 + iVar5,lVar4,0x30);
        lVar4 = DAT_ffff0000b811b0b8;
        iVar5 = *param_2 + 0x30;
        *param_2 = iVar5;
        if (*(int *)(lVar4 + 8) != 0) {
          uVar3 = 0;
          do {
            iVar9 = 0;
            do {
              lVar4 = *(long *)(*(long *)(DAT_ffff0000b811b0b8 + 0x30) + uVar3 * 0x10);
              *(uint *)(lVar4 + 0x888) = iVar9 + (*(uint *)(lVar4 + 0x888) & 7);
              local_44 = *(undefined4 *)(lVar4 + 0x88c);
              iVar5 = *param_2;
              if ((long)iVar5 - 0xffcU < 0xfffffffffffff000) goto LAB_ffff0000b8015eac;
              FUN_ffff0000b804cd40(lVar1 + iVar5,&local_44,4);
              iVar9 = iVar9 + 8;
              iVar5 = *param_2 + 4;
              *param_2 = iVar5;
            } while (iVar9 != 0x750);
            if (*(int *)(DAT_ffff0000b811b0b8 + 0xc) != 0) {
              uVar10 = 0;
              do {
                lVar4 = 0x864;
                do {
                  local_44 = *(undefined4 *)
                              (lVar4 + *(long *)(*(long *)(*(long *)(DAT_ffff0000b811b0b8 + 0x30) +
                                                           uVar3 * 0x10 + 8) + uVar10 * 8));
                  if ((long)iVar5 - 0xffcU < 0xfffffffffffff000) goto LAB_ffff0000b8015eac;
                  FUN_ffff0000b804cd40(lVar1 + iVar5,&local_44,4);
                  lVar4 = lVar4 + 4;
                  iVar5 = *param_2 + 4;
                  *param_2 = iVar5;
                } while (lVar4 != 0x998);
                uVar10 = uVar10 + 1;
              } while (uVar10 < *(uint *)(DAT_ffff0000b811b0b8 + 0xc));
            }
            uVar3 = uVar3 + 1;
          } while (uVar3 < *(uint *)(DAT_ffff0000b811b0b8 + 8));
        }
        if (iVar5 < 0x1001) {
          return 1;
        }
        uVar2 = 0;
      }
      if (0xfff < iVar5) {
        iVar5 = 0x1000;
      }
      *param_2 = iVar5;
    }
  }
  return uVar2;
}


