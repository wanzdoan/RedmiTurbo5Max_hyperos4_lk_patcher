
int FUN_ffff0000b8069524(void)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  ulong uVar4;
  int *piVar5;
  undefined4 local_14;
  
  iVar2 = 0;
  local_14 = 0;
  if ((DAT_ffff0000b81b7234 & 1) == 0) {
    if (DAT_ffff0000b81b7228 == 0) {
      iVar2 = FUN_ffff0000b8069050();
      if (iVar2 == 0) {
        iVar2 = 0x104009;
        if (DAT_ffff0000b81b722c == '\x01') {
          uVar4 = (ulong)DAT_ffff0000b81b7230;
          if (DAT_ffff0000b81b7230 != 0) {
            piVar5 = &DAT_ffff0000b8106d98;
            do {
              if (piVar5[-2] == 4) {
                DAT_ffff0000b81b7228 = *piVar5;
                goto LAB_ffff0000b8069558;
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
      iVar3 = FUN_ffff0000b8069ff4();
      if (iVar3 != 0) {
        FUN_ffff0000b804c33c
                  (s___s__Get_subgroup_index_failed__r_ffff0000b80995f3,&DAT_ffff0000b80987e6,iVar2)
        ;
      }
    }
    else {
LAB_ffff0000b8069558:
      iVar2 = FUN_ffff0000b8069a0c(DAT_ffff0000b81b7228,&local_14);
      if (iVar2 == 0) {
        iVar2 = FUN_ffff0000b8069ff4();
        if (iVar2 != 0) {
          FUN_ffff0000b804c33c(s___s__Misc_group_otp_ver__ffff0000b80920bf,&DAT_ffff0000b80987e6);
        }
        uVar1 = (local_14 & 0xff) - (local_14 >> 1 & 0x55);
        uVar1 = (uVar1 & 0x33333333) + (uVar1 >> 2 & 0x33333333);
        DAT_ffff0000b81076d0 = (undefined1)((uVar1 + (uVar1 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18);
        iVar2 = FUN_ffff0000b8069ff4();
        if (iVar2 != 0) {
          FUN_ffff0000b804c33c
                    (s___s__sub_group___d__sub_ver___d_ffff0000b808a50e,&DAT_ffff0000b80987e6,0,
                     DAT_ffff0000b81076d0);
        }
        uVar1 = (uint)local_14._1_1_ * 0x100 + (local_14._1_1_ & 0xaaaaaa) * -0x80;
        uVar1 = (uVar1 & 0x33333333) + (uVar1 >> 2 & 0x33333333);
        DAT_ffff0000b81076d2 = (undefined1)((uVar1 + (uVar1 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18);
        iVar2 = FUN_ffff0000b8069ff4();
        if (iVar2 != 0) {
          FUN_ffff0000b804c33c
                    (s___s__sub_group___d__sub_ver___d_ffff0000b808a50e,&DAT_ffff0000b80987e6,1,
                     DAT_ffff0000b81076d2);
        }
        uVar1 = (uint)local_14._2_1_ * 0x10000 + (local_14._2_1_ & 0xaaaa) * -0x8000;
        uVar1 = (uVar1 & 0x33333333) + (uVar1 >> 2 & 0x33333333);
        DAT_ffff0000b81076d4 = (undefined1)((uVar1 + (uVar1 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18);
        iVar2 = FUN_ffff0000b8069ff4();
        if (iVar2 != 0) {
          FUN_ffff0000b804c33c
                    (s___s__sub_group___d__sub_ver___d_ffff0000b808a50e,&DAT_ffff0000b80987e6,2,
                     DAT_ffff0000b81076d4);
        }
        uVar1 = (uint)local_14._3_1_ * 0x1000000 + (local_14._3_1_ & 0xaa) * -0x800000;
        uVar1 = (uVar1 & 0x33333333) + (uVar1 >> 2 & 0x33333333);
        DAT_ffff0000b81076d6 = (undefined1)((uVar1 + (uVar1 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18);
        iVar2 = FUN_ffff0000b8069ff4();
        if (iVar2 != 0) {
          FUN_ffff0000b804c33c
                    (s___s__sub_group___d__sub_ver___d_ffff0000b808a50e,&DAT_ffff0000b80987e6,3,
                     DAT_ffff0000b81076d6);
        }
        iVar2 = 0;
        DAT_ffff0000b81b7234 = 1;
      }
      else {
        iVar2 = 0x104004;
      }
    }
  }
  return iVar2;
}


