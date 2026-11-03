
int FUN_ffff0000be086f20(void)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  ulong uVar4;
  int *piVar5;
  undefined4 local_14;
  
  iVar2 = 0;
  local_14 = 0;
  if ((DAT_ffff0000be1b54f4 & 1) == 0) {
    if (DAT_ffff0000be1b54e8 == 0) {
      iVar2 = FUN_ffff0000be086a4c();
      if (iVar2 == 0) {
        iVar2 = 0x104009;
        if (DAT_ffff0000be1b54ec == '\x01') {
          uVar4 = (ulong)DAT_ffff0000be1b54f0;
          if (DAT_ffff0000be1b54f0 != 0) {
            piVar5 = &DAT_ffff0000be0f3740;
            do {
              if (piVar5[-2] == 4) {
                DAT_ffff0000be1b54e8 = *piVar5;
                goto LAB_ffff0000be086f54;
              }
              piVar5 = piVar5 + 7;
              uVar4 = uVar4 - 1;
            } while (uVar4 != 0);
          }
        }
        else {
          iVar2 = 0x10400c;
        }
      }
      iVar3 = FUN_ffff0000be0879f0();
      if (iVar3 != 0) {
        FUN_ffff0000be05fd98
                  (s___s__Get_subgroup_index_failed__r_ffff0000be0bc96b,&DAT_ffff0000be0bb811,iVar2)
        ;
      }
    }
    else {
LAB_ffff0000be086f54:
      iVar2 = FUN_ffff0000be087408(DAT_ffff0000be1b54e8,&local_14);
      if (iVar2 == 0) {
        iVar2 = FUN_ffff0000be0879f0();
        if (iVar2 != 0) {
          FUN_ffff0000be05fd98(s___s__Misc_group_otp_ver__ffff0000be0b390a,&DAT_ffff0000be0bb811);
        }
        uVar1 = (local_14 & 0xff) - (local_14 >> 1 & 0x55);
        uVar1 = (uVar1 & 0x33333333) + (uVar1 >> 2 & 0x33333333);
        DAT_ffff0000be0f4080 = (undefined1)((uVar1 + (uVar1 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18);
        iVar2 = FUN_ffff0000be0879f0();
        if (iVar2 != 0) {
          FUN_ffff0000be05fd98
                    (s___s__sub_group___d__sub_ver___d_ffff0000be0a9c37,&DAT_ffff0000be0bb811,0,
                     DAT_ffff0000be0f4080);
        }
        uVar1 = (uint)local_14._1_1_ * 0x100 + (local_14._1_1_ & 0xaaaaaa) * -0x80;
        uVar1 = (uVar1 & 0x33333333) + (uVar1 >> 2 & 0x33333333);
        DAT_ffff0000be0f4082 = (undefined1)((uVar1 + (uVar1 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18);
        iVar2 = FUN_ffff0000be0879f0();
        if (iVar2 != 0) {
          FUN_ffff0000be05fd98
                    (s___s__sub_group___d__sub_ver___d_ffff0000be0a9c37,&DAT_ffff0000be0bb811,1,
                     DAT_ffff0000be0f4082);
        }
        uVar1 = (uint)local_14._2_1_ * 0x10000 + (local_14._2_1_ & 0xaaaa) * -0x8000;
        uVar1 = (uVar1 & 0x33333333) + (uVar1 >> 2 & 0x33333333);
        DAT_ffff0000be0f4084 = (undefined1)((uVar1 + (uVar1 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18);
        iVar2 = FUN_ffff0000be0879f0();
        if (iVar2 != 0) {
          FUN_ffff0000be05fd98
                    (s___s__sub_group___d__sub_ver___d_ffff0000be0a9c37,&DAT_ffff0000be0bb811,2,
                     DAT_ffff0000be0f4084);
        }
        uVar1 = (uint)local_14._3_1_ * 0x1000000 + (local_14._3_1_ & 0xaa) * -0x800000;
        uVar1 = (uVar1 & 0x33333333) + (uVar1 >> 2 & 0x33333333);
        DAT_ffff0000be0f4086 = (undefined1)((uVar1 + (uVar1 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18);
        iVar2 = FUN_ffff0000be0879f0();
        if (iVar2 != 0) {
          FUN_ffff0000be05fd98
                    (s___s__sub_group___d__sub_ver___d_ffff0000be0a9c37,&DAT_ffff0000be0bb811,3,
                     DAT_ffff0000be0f4086);
        }
        iVar2 = 0;
        DAT_ffff0000be1b54f4 = 1;
      }
      else {
        iVar2 = 0x104004;
      }
    }
  }
  return iVar2;
}


