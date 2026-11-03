
int FUN_ffff0000907abf2c(void)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  ulong uVar4;
  int *piVar5;
  undefined4 local_14;
  
  iVar2 = 0;
  local_14 = 0;
  if ((DAT_ffff00009090aff8 & 1) == 0) {
    if (DAT_ffff00009090afec == 0) {
      iVar2 = FUN_ffff0000907aba58();
      if (iVar2 == 0) {
        iVar2 = 0x104009;
        if (DAT_ffff00009090aff0 == '\x01') {
          uVar4 = (ulong)DAT_ffff00009090aff4;
          if (DAT_ffff00009090aff4 != 0) {
            piVar5 = &DAT_ffff000090837568;
            do {
              if (piVar5[-2] == 4) {
                DAT_ffff00009090afec = *piVar5;
                goto LAB_ffff0000907abf60;
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
      iVar3 = FUN_ffff0000907ad7fc();
      if (iVar3 != 0) {
        FUN_ffff00009077e11c
                  (s___s__Get_subgroup_index_failed__r_ffff0000907f3b19,&DAT_ffff0000907f228a,iVar2)
        ;
      }
    }
    else {
LAB_ffff0000907abf60:
      iVar2 = FUN_ffff0000907acf10(DAT_ffff00009090afec,&local_14);
      if (iVar2 == 0) {
        iVar2 = FUN_ffff0000907ad7fc();
        if (iVar2 != 0) {
          FUN_ffff00009077e11c(s___s__Misc_group_otp_ver__ffff0000907e70a4,&DAT_ffff0000907f228a);
        }
        uVar1 = (local_14 & 0xff) - (local_14 >> 1 & 0x55);
        uVar1 = (uVar1 & 0x33333333) + (uVar1 >> 2 & 0x33333333);
        DAT_ffff000090837ea8 = (undefined1)((uVar1 + (uVar1 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18);
        iVar2 = FUN_ffff0000907ad7fc();
        if (iVar2 != 0) {
          FUN_ffff00009077e11c
                    (s___s__sub_group___d__sub_ver___d_ffff0000907d8927,&DAT_ffff0000907f228a,0,
                     DAT_ffff000090837ea8);
        }
        uVar1 = (uint)local_14._1_1_ * 0x100 + (local_14._1_1_ & 0xaaaaaa) * -0x80;
        uVar1 = (uVar1 & 0x33333333) + (uVar1 >> 2 & 0x33333333);
        DAT_ffff000090837eaa = (undefined1)((uVar1 + (uVar1 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18);
        iVar2 = FUN_ffff0000907ad7fc();
        if (iVar2 != 0) {
          FUN_ffff00009077e11c
                    (s___s__sub_group___d__sub_ver___d_ffff0000907d8927,&DAT_ffff0000907f228a,1,
                     DAT_ffff000090837eaa);
        }
        uVar1 = (uint)local_14._2_1_ * 0x10000 + (local_14._2_1_ & 0xaaaa) * -0x8000;
        uVar1 = (uVar1 & 0x33333333) + (uVar1 >> 2 & 0x33333333);
        DAT_ffff000090837eac = (undefined1)((uVar1 + (uVar1 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18);
        iVar2 = FUN_ffff0000907ad7fc();
        if (iVar2 != 0) {
          FUN_ffff00009077e11c
                    (s___s__sub_group___d__sub_ver___d_ffff0000907d8927,&DAT_ffff0000907f228a,2,
                     DAT_ffff000090837eac);
        }
        uVar1 = (uint)local_14._3_1_ * 0x1000000 + (local_14._3_1_ & 0xaa) * -0x800000;
        uVar1 = (uVar1 & 0x33333333) + (uVar1 >> 2 & 0x33333333);
        DAT_ffff000090837eae = (undefined1)((uVar1 + (uVar1 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18);
        iVar2 = FUN_ffff0000907ad7fc();
        if (iVar2 != 0) {
          FUN_ffff00009077e11c
                    (s___s__sub_group___d__sub_ver___d_ffff0000907d8927,&DAT_ffff0000907f228a,3,
                     DAT_ffff000090837eae);
        }
        iVar2 = 0;
        DAT_ffff00009090aff8 = 1;
      }
      else {
        iVar2 = 0x104004;
      }
    }
  }
  return iVar2;
}


