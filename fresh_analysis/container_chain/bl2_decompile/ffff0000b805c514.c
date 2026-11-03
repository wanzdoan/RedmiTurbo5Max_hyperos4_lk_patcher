
undefined8 FUN_ffff0000b805c514(ulong *param_1,long param_2)

{
  long lVar1;
  uint uVar2;
  int iVar3;
  ulong uVar4;
  long lVar5;
  long lVar6;
  uint uVar7;
  ulong uVar8;
  ulong uVar9;
  ulong uVar10;
  uint local_34;
  ulong local_18;
  
  if (param_1 == (ulong *)0x0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b805b6c8
              (s_md____NULL_ffff0000b8096c14,s_platform_mediatek_mt6991_sboot_c_ffff0000b8095c98,
               0xf1);
  }
  if (param_2 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b805b6c8
              (s_out____NULL_ffff0000b809bfca,s_platform_mediatek_mt6991_sboot_c_ffff0000b8095c98,
               0xf2);
  }
  uVar2 = (uint)param_1[5];
  uVar4 = (ulong)uVar2;
  if (0x3f < uVar2) {
    return 0x10;
  }
  uVar9 = *param_1;
  uVar7 = uVar2 + 1;
  lVar1 = (long)param_1 + 0x2c;
  *(uint *)(param_1 + 5) = uVar7;
  *(undefined1 *)(lVar1 + uVar4) = 0x80;
  *param_1 = uVar9 + uVar4 * 8;
  if (uVar2 < 0x38) {
    if (uVar2 == 0x37) goto LAB_ffff0000b805c610;
  }
  else {
    if (uVar2 < 0x3f) {
      lVar5 = uVar4 + 1;
      uVar9 = 0x41 - (ulong)(uVar2 + 2);
      if (((uVar9 < 2) || (uVar8 = 0x40 - (ulong)(uVar2 + 2), -uVar2 - 3 < (uint)uVar8)) ||
         (uVar8 >> 0x20 != 0)) {
LAB_ffff0000b805c598:
        lVar5 = lVar5 + 0x2c;
        do {
          *(undefined1 *)((long)param_1 + lVar5) = 0;
          iVar3 = (int)lVar5;
          lVar5 = lVar5 + 1;
        } while (iVar3 != 0x6b);
      }
      else {
        uVar8 = uVar9 & 0xfffffffffffffffe;
        lVar5 = lVar5 + uVar8;
        lVar6 = (long)param_1 + uVar4 + 0x2e;
        uVar4 = uVar8;
        do {
          *(undefined2 *)(lVar6 + -1) = 0;
          uVar4 = uVar4 - 2;
          lVar6 = lVar6 + 2;
        } while (uVar4 != 0);
        if (uVar9 != uVar8) goto LAB_ffff0000b805c598;
      }
      *(undefined4 *)(param_1 + 5) = 0x40;
    }
    FUN_ffff0000b805c9b0(param_1,lVar1);
    uVar7 = 0;
  }
  uVar4 = (ulong)uVar7;
  uVar9 = 0x39 - (ulong)(uVar7 + 1);
  if (((uVar9 < 2) || (uVar8 = 0x38 - (ulong)(uVar7 + 1), -uVar7 - 2 < (uint)uVar8)) ||
     (uVar8 >> 0x20 != 0)) {
LAB_ffff0000b805c5f0:
    lVar5 = uVar4 + 0x2c;
    do {
      *(undefined1 *)((long)param_1 + lVar5) = 0;
      iVar3 = (int)lVar5;
      lVar5 = lVar5 + 1;
    } while (iVar3 != 99);
  }
  else {
    uVar10 = uVar9 & 0xfffffffffffffffe;
    lVar5 = (long)param_1 + uVar4 + 0x2d;
    uVar8 = uVar10;
    do {
      *(undefined2 *)(lVar5 + -1) = 0;
      uVar8 = uVar8 - 2;
      lVar5 = lVar5 + 2;
    } while (uVar8 != 0);
    uVar4 = uVar10 + uVar4;
    if (uVar9 != uVar10) goto LAB_ffff0000b805c5f0;
  }
  *(undefined4 *)(param_1 + 5) = 0x38;
LAB_ffff0000b805c610:
  uVar4 = (*param_1 & 0xff00ff00ff00ff00) >> 8 | (*param_1 & 0xff00ff00ff00ff) << 8;
  uVar4 = (uVar4 & 0xffff0000ffff0000) >> 0x10 | (uVar4 & 0xffff0000ffff) << 0x10;
  local_18 = uVar4 >> 0x20 | uVar4 << 0x20;
  FUN_ffff0000b804cd40((long)param_1 + 100,&local_18,8);
  FUN_ffff0000b805c9b0(param_1,lVar1);
  uVar2 = ((uint)param_1[1] & 0xff00ff00) >> 8 | ((uint)param_1[1] & 0xff00ff) << 8;
  local_34 = uVar2 >> 0x10 | uVar2 << 0x10;
  FUN_ffff0000b804cd40(param_2,&local_34,4);
  uVar2 = (*(uint *)((long)param_1 + 0xc) & 0xff00ff00) >> 8 |
          (*(uint *)((long)param_1 + 0xc) & 0xff00ff) << 8;
  local_34 = uVar2 >> 0x10 | uVar2 << 0x10;
  FUN_ffff0000b804cd40(param_2 + 4,&local_34,4);
  uVar2 = ((uint)param_1[2] & 0xff00ff00) >> 8 | ((uint)param_1[2] & 0xff00ff) << 8;
  local_34 = uVar2 >> 0x10 | uVar2 << 0x10;
  FUN_ffff0000b804cd40(param_2 + 8,&local_34,4);
  uVar2 = (*(uint *)((long)param_1 + 0x14) & 0xff00ff00) >> 8 |
          (*(uint *)((long)param_1 + 0x14) & 0xff00ff) << 8;
  local_34 = uVar2 >> 0x10 | uVar2 << 0x10;
  FUN_ffff0000b804cd40(param_2 + 0xc,&local_34,4);
  uVar2 = ((uint)param_1[3] & 0xff00ff00) >> 8 | ((uint)param_1[3] & 0xff00ff) << 8;
  local_34 = uVar2 >> 0x10 | uVar2 << 0x10;
  FUN_ffff0000b804cd40(param_2 + 0x10,&local_34,4);
  uVar2 = (*(uint *)((long)param_1 + 0x1c) & 0xff00ff00) >> 8 |
          (*(uint *)((long)param_1 + 0x1c) & 0xff00ff) << 8;
  local_34 = uVar2 >> 0x10 | uVar2 << 0x10;
  FUN_ffff0000b804cd40(param_2 + 0x14,&local_34,4);
  uVar2 = ((uint)param_1[4] & 0xff00ff00) >> 8 | ((uint)param_1[4] & 0xff00ff) << 8;
  local_34 = uVar2 >> 0x10 | uVar2 << 0x10;
  FUN_ffff0000b804cd40(param_2 + 0x18,&local_34,4);
  uVar2 = (*(uint *)((long)param_1 + 0x24) & 0xff00ff00) >> 8 |
          (*(uint *)((long)param_1 + 0x24) & 0xff00ff) << 8;
  local_34 = uVar2 >> 0x10 | uVar2 << 0x10;
  FUN_ffff0000b804cd40(param_2 + 0x1c,&local_34,4);
  FUN_ffff0000b805e5ac(param_1,0x70);
  return 0;
}


