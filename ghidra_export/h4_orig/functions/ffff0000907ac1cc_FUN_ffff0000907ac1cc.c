// ffff0000907ac1cc  size=584  FUN_ffff0000907ac1cc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

ulong FUN_ffff0000907ac1cc(uint param_1,long param_2)

{
  char cVar1;
  int iVar2;
  uint uVar3;
  ulong uVar4;
  uint *puVar5;
  uint uVar6;
  uint uVar7;
  
  if (param_1 == 4) {
    cVar1 = FUN_ffff000090798940(param_2);
    if (cVar1 != '\x04') {
      uVar4 = FUN_ffff0000907abdf0();
      return uVar4;
    }
    if ((param_2 == 0) ||
       ((iVar2 = FUN_ffff00009077f0ac(param_2,PTR_s_lk_main_dtb_ffff000090837e98), iVar2 != 0 &&
        (iVar2 = FUN_ffff00009077f0ac(param_2,PTR_DAT_ffff000090837ea0), iVar2 != 0)))) {
      iVar2 = FUN_ffff0000907ad7fc();
      if (iVar2 == 0) {
        uVar3 = 0x10400f;
      }
      else {
        FUN_ffff00009077e11c
                  (s___s___s_not_defined_in_sub_table_ffff0000907f75b4,&DAT_ffff0000907f228a,param_2
                  );
        uVar3 = 0x10400f;
      }
      goto LAB_ffff0000907ac388;
    }
  }
  else {
    uVar3 = FUN_ffff0000907aba58();
    if (uVar3 == 0) {
      uVar3 = 0x104009;
      uVar6 = *(uint *)(DAT_ffff000090839aa0 + 0x454);
      uVar7 = *(uint *)(DAT_ffff000090839ab0 + 0x70);
      if (DAT_ffff00009090aff0 == '\x01') {
        if (_DAT_ffff00009090aff4 != 0) {
          uVar4 = 0;
          puVar5 = (uint *)(PTR_DAT_ffff000090839a80 + 0x18);
          do {
            if (puVar5[-6] == param_1) {
              if (uVar6 == uVar7) {
                uVar7 = uVar6;
                if (uVar6 < puVar5[-2]) {
                  uVar3 = 0x104001;
                }
                else if (puVar5[-1] < uVar6) {
                  uVar3 = 0x104002;
                }
                else if (uVar6 < *puVar5) {
                  uVar3 = 0;
                  *puVar5 = uVar6;
                }
                else {
                  uVar3 = 0;
                }
              }
              else {
                uVar3 = 0x104000;
              }
              goto LAB_ffff0000907ac314;
            }
            uVar4 = uVar4 + 1;
            puVar5 = puVar5 + 7;
          } while (_DAT_ffff00009090aff4 != uVar4);
        }
        uVar4 = 1;
      }
      else {
        uVar4 = 0;
        uVar3 = 0x10400c;
      }
    }
    else {
      uVar6 = 0;
      uVar4 = 0;
      param_1 = 0;
      uVar7 = 0;
    }
LAB_ffff0000907ac314:
    iVar2 = FUN_ffff0000907ad7fc();
    if (iVar2 != 0) {
      FUN_ffff00009077e11c
                (s___s__img_ver_check___group__key_v_ffff0000907e70be,&DAT_ffff0000907f228a,param_1,
                 uVar6,uVar7,
                 *(undefined4 *)(PTR_DAT_ffff000090839a80 + (uVar4 & 0xffffffff) * 0x1c + 0x10));
    }
    iVar2 = FUN_ffff0000907ad7fc();
    if (uVar3 != 0) {
      if (iVar2 != 0) {
        FUN_ffff00009077e11c(s____fail__0x_x__ffff000090803491,uVar3);
      }
      goto LAB_ffff0000907ac388;
    }
    if (iVar2 != 0) {
      FUN_ffff00009077e11c(s____ok_ffff000090801b14);
    }
  }
  uVar3 = 0;
LAB_ffff0000907ac388:
  return (ulong)uVar3;
}


