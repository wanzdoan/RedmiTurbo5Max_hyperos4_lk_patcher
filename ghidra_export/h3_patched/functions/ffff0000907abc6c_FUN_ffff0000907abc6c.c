// ffff0000907abc6c  size=672  FUN_ffff0000907abc6c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907abc6c(void)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  ulong uVar4;
  int *piVar5;
  undefined4 local_14;
  
  iVar2 = 0;
  local_14 = 0;
  if ((DAT_ffff00009090af38 & 1) == 0) {
    if (_DAT_ffff00009090af2c == 0) {
      iVar2 = FUN_ffff0000907ab798();
      if (iVar2 == 0) {
        iVar2 = 0x104009;
        if (DAT_ffff00009090af30 == '\x01') {
          uVar4 = (ulong)_DAT_ffff00009090af34;
          if (_DAT_ffff00009090af34 != 0) {
            piVar5 = &DAT_ffff000090837548;
            do {
              if (piVar5[-2] == 4) {
                _DAT_ffff00009090af2c = *piVar5;
                goto LAB_ffff0000907abca0;
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
      iVar3 = FUN_ffff0000907ad53c();
      if (iVar3 != 0) {
        FUN_ffff00009077de54
                  (s___s__Get_subgroup_index_failed__r_ffff0000907f3976,&DAT_ffff0000907f20e7,iVar2)
        ;
      }
    }
    else {
LAB_ffff0000907abca0:
      iVar2 = FUN_ffff0000907acc50(_DAT_ffff00009090af2c,&local_14);
      if (iVar2 == 0) {
        iVar2 = FUN_ffff0000907ad53c();
        if (iVar2 != 0) {
          FUN_ffff00009077de54(s___s__Misc_group_otp_ver__ffff0000907e6f49,&DAT_ffff0000907f20e7);
        }
        uVar1 = (local_14 & 0xff) - (local_14 >> 1 & 0x55);
        uVar1 = (uVar1 & 0x33333333) + (uVar1 >> 2 & 0x33333333);
        DAT_ffff000090837e88 = (undefined1)((uVar1 + (uVar1 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18);
        iVar2 = FUN_ffff0000907ad53c();
        if (iVar2 != 0) {
          FUN_ffff00009077de54
                    (s___s__sub_group___d__sub_ver___d_ffff0000907d8891,&DAT_ffff0000907f20e7,0,
                     DAT_ffff000090837e88);
        }
        uVar1 = (uint)local_14._1_1_ * 0x100 + (local_14._1_1_ & 0xaaaaaa) * -0x80;
        uVar1 = (uVar1 & 0x33333333) + (uVar1 >> 2 & 0x33333333);
        DAT_ffff000090837e8a = (undefined1)((uVar1 + (uVar1 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18);
        iVar2 = FUN_ffff0000907ad53c();
        if (iVar2 != 0) {
          FUN_ffff00009077de54
                    (s___s__sub_group___d__sub_ver___d_ffff0000907d8891,&DAT_ffff0000907f20e7,1,
                     DAT_ffff000090837e8a);
        }
        uVar1 = (uint)local_14._2_1_ * 0x10000 + (local_14._2_1_ & 0xaaaa) * -0x8000;
        uVar1 = (uVar1 & 0x33333333) + (uVar1 >> 2 & 0x33333333);
        DAT_ffff000090837e8c = (undefined1)((uVar1 + (uVar1 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18);
        iVar2 = FUN_ffff0000907ad53c();
        if (iVar2 != 0) {
          FUN_ffff00009077de54
                    (s___s__sub_group___d__sub_ver___d_ffff0000907d8891,&DAT_ffff0000907f20e7,2,
                     DAT_ffff000090837e8c);
        }
        uVar1 = (uint)local_14._3_1_ * 0x1000000 + (local_14._3_1_ & 0xaa) * -0x800000;
        uVar1 = (uVar1 & 0x33333333) + (uVar1 >> 2 & 0x33333333);
        DAT_ffff000090837e8e = (undefined1)((uVar1 + (uVar1 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18);
        iVar2 = FUN_ffff0000907ad53c();
        if (iVar2 != 0) {
          FUN_ffff00009077de54
                    (s___s__sub_group___d__sub_ver___d_ffff0000907d8891,&DAT_ffff0000907f20e7,3,
                     DAT_ffff000090837e8e);
        }
        iVar2 = 0;
        DAT_ffff00009090af38 = 1;
      }
      else {
        iVar2 = 0x104004;
      }
    }
  }
  return iVar2;
}


