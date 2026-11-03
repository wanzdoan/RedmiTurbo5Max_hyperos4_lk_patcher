
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be04a6c4(void)

{
  ulong uVar1;
  uint uVar2;
  uint uVar3;
  bool bVar4;
  int iVar5;
  int iVar6;
  ulong uVar7;
  uint uVar8;
  uint uVar9;
  long lVar10;
  ulong uVar11;
  undefined4 *puVar12;
  long lVar13;
  long lVar14;
  uint *puVar15;
  long *plVar16;
  ulong uVar17;
  
  FUN_ffff0000be05fd98
            (s__Thermal________s_begin________ffff0000be0b8ff0,s_lvts_thermal_init_ffff0000be0a9646)
  ;
  if (((((DAT_ffff0000be0de290 == 0) ||
        ((*(uint *)((ulong)DAT_ffff0000be0de298 + DAT_ffff0000be0de2a0 + 0xf0) & 0xfff) == 0x555))
       && ((DAT_ffff0000be0de2e0 == 0 ||
           ((*(uint *)((ulong)DAT_ffff0000be0de2e8 + DAT_ffff0000be0de2f0 + 0xf0) & 0xfff) == 0x555)
           ))) &&
      ((DAT_ffff0000be0de330 == 0 ||
       ((*(uint *)((ulong)DAT_ffff0000be0de338 + DAT_ffff0000be0de340 + 0xf0) & 0xfff) == 0x555))))
     && ((DAT_ffff0000be0de380 == 0 ||
         ((*(uint *)((ulong)DAT_ffff0000be0de388 + DAT_ffff0000be0de390 + 0xf0) & 0xfff) == 0x555)))
     ) {
    FUN_ffff0000be05fd98(s__Thermal_LK_init_LVTS_ffff0000be0c0fea);
    return;
  }
  iVar5 = FUN_ffff0000be019910();
  _DAT_ffff000014461f30 = 0x800;
  _DAT_ffff000014461f34 = 0x800;
  FUN_ffff0000be04b634();
  uVar11 = 0;
  do {
    if ((&DAT_ffff0000be0de290)[uVar11 * 0x14] != 0) {
      puVar15 = &DAT_ffff0000be0de298 + uVar11 * 0x14;
      plVar16 = &DAT_ffff0000be0de2a0 + uVar11 * 10;
      lVar10 = *plVar16 + (ulong)*puVar15;
      *(undefined4 *)(lVar10 + 0xe4) = 1;
      *(undefined4 *)(lVar10 + 0x68) = 0x1108;
      FUN_ffff0000be05ede4(10);
      lVar14 = *plVar16;
      lVar13 = (ulong)*puVar15 + 0x50;
      *(undefined4 *)(lVar13 + lVar14) = 0x8103ffff;
      FUN_ffff0000be05ede4(3);
      if ((*(uint *)(lVar13 + lVar14) >> 0x18 & 1) != 0) {
        iVar6 = 100;
        do {
          iVar6 = iVar6 + -1;
          if (iVar6 == 0) {
            FUN_ffff0000be05fd98(s__Thermal_write_err__DEVICE_ACCES_ffff0000be0a85d7,0xff);
            break;
          }
          FUN_ffff0000be05ede4(2);
        } while ((*(uint *)(lVar14 + lVar13) >> 0x18 & 1) != 0);
      }
      lVar14 = *plVar16;
      lVar13 = (ulong)*puVar15 + 0x50;
      *(undefined4 *)(lVar13 + lVar14) = 0x8502fc55;
      FUN_ffff0000be05ede4(3);
      if ((*(uint *)(lVar13 + lVar14) >> 0x18 & 1) != 0) {
        iVar6 = 100;
        do {
          iVar6 = iVar6 + -1;
          if (iVar6 == 0) {
            FUN_ffff0000be05fd98(s__Thermal_write_err__DEVICE_ACCES_ffff0000be0a85d7,0xfc);
            break;
          }
          FUN_ffff0000be05ede4(2);
        } while ((*(uint *)(lVar14 + lVar13) >> 0x18 & 1) != 0);
      }
      puVar15 = (uint *)(lVar10 + 0x4c);
      uVar7 = uVar11 + 0x8b;
      uVar17 = (ulong)*puVar15 & 0xff;
      if (uVar7 != uVar17) {
        FUN_ffff0000be05ede4(3);
        uVar17 = (ulong)*puVar15 & 0xff;
        if (uVar7 != uVar17) {
          FUN_ffff0000be05ede4(3);
          uVar17 = (ulong)*puVar15 & 0xff;
          if (uVar7 != uVar17) {
            FUN_ffff0000be05ede4(3);
            uVar17 = (ulong)*puVar15 & 0xff;
            if (uVar7 != uVar17) {
              FUN_ffff0000be05ede4(3);
              uVar17 = (ulong)*puVar15 & 0xff;
              if (uVar7 != uVar17) {
                FUN_ffff0000be05ede4(3);
                FUN_ffff0000be05fd98
                          (s__Thermal_LVTS_TC__d__addr_0x_x__D_ffff0000be0c45ef,uVar11 & 0xffffffff,
                           (ulong)puVar15 & 0xffffffff,(int)uVar11 + 0x8b,uVar17);
                goto LAB_ffff0000be04a848;
              }
            }
          }
        }
      }
      FUN_ffff0000be05fd98
                (s__Thermal_LVTS_TC__d__ID_is_0x_x_ffff0000be0bfd3d,uVar11 & 0xffffffff,uVar17);
    }
LAB_ffff0000be04a848:
    uVar11 = uVar11 + 1;
  } while (uVar11 != 4);
  lVar13 = 0;
  do {
    if ((&DAT_ffff0000be0de290)[lVar13 * 0x14] != 0) {
      puVar15 = &DAT_ffff0000be0de298 + lVar13 * 0x14;
      lVar10 = (ulong)*puVar15 + 0x50;
      plVar16 = &DAT_ffff0000be0de2a0 + lVar13 * 10;
      lVar14 = *plVar16;
      *(undefined4 *)(lVar10 + lVar14) = 0x81030300;
      FUN_ffff0000be05ede4(3);
      if ((*(uint *)(lVar10 + lVar14) >> 0x18 & 1) != 0) {
        iVar6 = 100;
        do {
          iVar6 = iVar6 + -1;
          if (iVar6 == 0) {
            FUN_ffff0000be05fd98(s__Thermal_write_err__DEVICE_ACCES_ffff0000be0a85d7,3);
            uVar9 = (&DAT_ffff0000be1261d0)[lVar13];
            goto joined_r0xffff0000be04aab0;
          }
          FUN_ffff0000be05ede4(2);
        } while ((*(uint *)(lVar14 + lVar10) >> 0x18 & 1) != 0);
      }
      uVar9 = (&DAT_ffff0000be1261d0)[lVar13];
joined_r0xffff0000be04aab0:
      if (uVar9 == 0) {
        uVar8 = 0;
        uVar9 = 0xa0;
      }
      else {
        if ((uVar9 >> 5 & 1) == 0) {
          uVar8 = uVar9 & 0xf;
          uVar9 = uVar9 >> 1 & 8;
        }
        else {
          uVar8 = (uVar9 & 0xf) << 4;
          uVar9 = uVar9 & 0x10;
        }
        uVar9 = uVar9 | 0xa0;
      }
      lVar14 = *plVar16;
      lVar10 = (ulong)*puVar15 + 0x50;
      *(uint *)(lVar10 + lVar14) = uVar8 | 0x81032000;
      FUN_ffff0000be05ede4(3);
      if ((*(uint *)(lVar10 + lVar14) >> 0x18 & 1) != 0) {
        iVar6 = 100;
        do {
          iVar6 = iVar6 + -1;
          if (iVar6 == 0) {
            FUN_ffff0000be05fd98(s__Thermal_write_err__DEVICE_ACCES_ffff0000be0a85d7,0x20);
            break;
          }
          FUN_ffff0000be05ede4(2);
        } while ((*(uint *)(lVar14 + lVar10) >> 0x18 & 1) != 0);
      }
      lVar14 = *plVar16;
      lVar10 = (ulong)*puVar15 + 0x50;
      *(uint *)(lVar10 + lVar14) = uVar9 | 0x81032100;
      FUN_ffff0000be05ede4(3);
      if ((*(uint *)(lVar10 + lVar14) >> 0x18 & 1) != 0) {
        iVar6 = 100;
        do {
          iVar6 = iVar6 + -1;
          if (iVar6 == 0) {
            FUN_ffff0000be05fd98(s__Thermal_write_err__DEVICE_ACCES_ffff0000be0a85d7,0x21);
            break;
          }
          FUN_ffff0000be05ede4(2);
        } while ((*(uint *)(lVar14 + lVar10) >> 0x18 & 1) != 0);
      }
    }
    lVar13 = lVar13 + 1;
  } while (lVar13 != 4);
  lVar13 = 0;
  puVar12 = &DAT_ffff0000be0de270;
  do {
    if ((&DAT_ffff0000be0de290)[lVar13 * 0x14] != 0) {
      lVar10 = (&DAT_ffff0000be0de2a0)[lVar13 * 10] + (ulong)(&DAT_ffff0000be0de298)[lVar13 * 0x14];
      if ((&DAT_ffff0000be0de294)[lVar13 * 0x14] == 0) {
        uVar8 = 0;
        uVar9 = 0;
      }
      else {
        uVar11 = 0;
        uVar8 = 0;
        uVar9 = 0;
        do {
          if ((puVar12[uVar11 + 4] != 0) && ((uint)uVar11 < 4)) {
            uVar2 = puVar12[uVar11];
            iVar6 = (&DAT_ffff0000be126188)[uVar2] + 0xfc28;
            switch(uVar11 & 0xffffffff) {
            case 0:
              *(int *)(lVar10 + 0x54) = iVar6;
              uVar9 = ((&DAT_ffff0000be126150)[uVar2] + 500) / 1000 | uVar9;
              break;
            case 1:
              *(int *)(lVar10 + 0x58) = iVar6;
              uVar9 = uVar9 | ((uint)((ulong)((long)((&DAT_ffff0000be126150)[uVar2] + 500) *
                                             0x10624dd3) >> 0x26) -
                              ((&DAT_ffff0000be126150)[uVar2] + 500 >> 0x1f)) * 0x400;
              break;
            case 2:
              *(int *)(lVar10 + 0x5c) = iVar6;
              uVar8 = ((&DAT_ffff0000be126150)[uVar2] + 500) / 1000 | uVar8;
              break;
            case 3:
              *(int *)(lVar10 + 0x60) = iVar6;
              uVar8 = uVar8 | ((uint)((ulong)((long)((&DAT_ffff0000be126150)[uVar2] + 500) *
                                             0x10624dd3) >> 0x26) -
                              ((&DAT_ffff0000be126150)[uVar2] + 500 >> 0x1f)) * 0x400;
            }
          }
          uVar11 = uVar11 + 1;
        } while (uVar11 < (&DAT_ffff0000be0de294)[lVar13 * 0x14]);
      }
      uVar9 = uVar9 | DAT_ffff0000be1260dc << 0x14;
      FUN_ffff0000be05fd98(s_lvts_spare_setting__0x_x__0x_x_ffff0000be0a9658,uVar9,uVar8);
      *(undefined4 *)(lVar10 + 0x6c) = 0xfc28;
      *(uint *)(lVar10 + 0xf4) = uVar9;
      *(uint *)(lVar10 + 0xf8) = uVar8;
    }
    iVar6 = DAT_ffff0000be0de290;
    lVar13 = lVar13 + 1;
    puVar12 = puVar12 + 0x14;
  } while (lVar13 != 4);
  if (DAT_ffff0000be0de290 != 0) {
    *(undefined4 *)(DAT_ffff0000be0de2a0 + (ulong)DAT_ffff0000be0de298) = 0x200;
  }
  if (DAT_ffff0000be0de2e0 != 0) {
    *(undefined4 *)(DAT_ffff0000be0de2f0 + (ulong)DAT_ffff0000be0de2e8) = 0x200;
  }
  if (DAT_ffff0000be0de330 != 0) {
    *(undefined4 *)(DAT_ffff0000be0de340 + (ulong)DAT_ffff0000be0de338) = 0x200;
  }
  if (DAT_ffff0000be0de380 != 0) {
    *(undefined4 *)(DAT_ffff0000be0de390 + (ulong)DAT_ffff0000be0de388) = 0x200;
  }
  bVar4 = true;
  do {
    lVar13 = DAT_ffff0000be0de2a0;
    if (iVar6 == 0) {
LAB_ffff0000be04ae0c:
      lVar13 = DAT_ffff0000be0de2f0;
      if (DAT_ffff0000be0de2e0 != 0) {
        iVar6 = 100;
        uVar11 = (ulong)DAT_ffff0000be0de2e8;
        do {
          uVar9 = *(uint *)(lVar13 + uVar11 + 0x3c);
          if ((uVar9 & 0x481) == 0) goto LAB_ffff0000be04ae5c;
          FUN_ffff0000be05ede4(2);
          iVar6 = iVar6 + -1;
        } while (iVar6 != 0);
        FUN_ffff0000be05fd98(s__Thermal_Error_LVTS__d_sensing_p_ffff0000be0c1001,1,uVar9);
LAB_ffff0000be04ae5c:
        if (((uVar9 & 1) != 0 || (uVar9 >> 7 & 1) != 0) || (uVar9 >> 8 & 4) != 0)
        goto LAB_ffff0000be04ada0;
      }
      lVar13 = DAT_ffff0000be0de340;
      if (DAT_ffff0000be0de330 != 0) {
        iVar6 = 100;
        uVar11 = (ulong)DAT_ffff0000be0de338;
        do {
          uVar9 = *(uint *)(lVar13 + uVar11 + 0x3c);
          if ((uVar9 & 0x481) == 0) goto LAB_ffff0000be04aec8;
          FUN_ffff0000be05ede4(2);
          iVar6 = iVar6 + -1;
        } while (iVar6 != 0);
        FUN_ffff0000be05fd98(s__Thermal_Error_LVTS__d_sensing_p_ffff0000be0c1001,2,uVar9);
LAB_ffff0000be04aec8:
        if (((uVar9 & 1) != 0 || (uVar9 >> 7 & 1) != 0) || (uVar9 >> 8 & 4) != 0)
        goto LAB_ffff0000be04ada0;
      }
      lVar13 = DAT_ffff0000be0de390;
      if (DAT_ffff0000be0de380 == 0) break;
      iVar6 = 100;
      uVar11 = (ulong)DAT_ffff0000be0de388;
      do {
        uVar9 = *(uint *)(lVar13 + uVar11 + 0x3c);
        if ((uVar9 & 0x481) == 0) goto LAB_ffff0000be04af34;
        FUN_ffff0000be05ede4(2);
        iVar6 = iVar6 + -1;
      } while (iVar6 != 0);
      FUN_ffff0000be05fd98(s__Thermal_Error_LVTS__d_sensing_p_ffff0000be0c1001,3,uVar9);
LAB_ffff0000be04af34:
      if (((uVar9 & 1) == 0 && (uVar9 >> 7 & 1) == 0) && (uVar9 >> 8 & 4) == 0 || !bVar4) break;
    }
    else {
      uVar11 = (ulong)DAT_ffff0000be0de298;
      iVar6 = 100;
      do {
        uVar9 = *(uint *)(lVar13 + uVar11 + 0x3c);
        if ((uVar9 & 0x481) == 0) goto LAB_ffff0000be04adf0;
        FUN_ffff0000be05ede4(2);
        iVar6 = iVar6 + -1;
      } while (iVar6 != 0);
      FUN_ffff0000be05fd98(s__Thermal_Error_LVTS__d_sensing_p_ffff0000be0c1001,0,uVar9);
LAB_ffff0000be04adf0:
      if (((uVar9 & 1) == 0 && (uVar9 >> 7 & 1) == 0) && (uVar9 >> 8 & 4) == 0)
      goto LAB_ffff0000be04ae0c;
LAB_ffff0000be04ada0:
      if (!bVar4) break;
    }
    bVar4 = false;
    iVar6 = DAT_ffff0000be0de290;
  } while( true );
  uVar11 = 0;
  puVar15 = &DAT_ffff0000be0de298;
  do {
    if (puVar15[-2] != 0) {
      lVar10 = *(long *)(puVar15 + 2);
      lVar13 = lVar10 + (ulong)*puVar15;
      *(undefined4 *)(lVar13 + 0x40) = 0x13121110;
      *(undefined4 *)(lVar13 + 0x48) = 0x300;
      uVar9 = puVar15[8];
      uVar8 = puVar15[9];
      lVar10 = lVar10 + (ulong)*puVar15;
      *(uint *)(lVar10 + 4) = puVar15[7] & 0x3ff | puVar15[6] << 0x11;
      *(uint *)(lVar10 + 8) = uVar8 & 0x3ff | (uVar9 & 0x3ff) << 0x10;
      *(undefined4 *)(lVar10 + 0x38) = 0;
      FUN_ffff0000be05ede4(1);
      FUN_ffff0000be05fd98
                (s__Thermal__s__d__LVTSMONCTL1_0__0_ffff0000be0c33d5,
                 s_lvts_configure_polling_speed_and_ffff0000be0b3388,uVar11 & 0xffffffff,
                 *(undefined4 *)(lVar10 + 4),*(undefined4 *)(lVar10 + 8),
                 *(undefined4 *)(lVar10 + 0x38));
    }
    uVar11 = uVar11 + 1;
    puVar15 = puVar15 + 0x14;
  } while (uVar11 != 4);
  uVar11 = 0;
  puVar12 = &DAT_ffff0000be0de270;
  do {
    if ((&DAT_ffff0000be0de290)[uVar11 * 0x14] != 0) {
      uVar7 = (ulong)(uint)(&DAT_ffff0000be0de2ac)[uVar11 * 0x14];
      puVar15 = &DAT_ffff0000be0de294 + uVar11 * 0x14;
      uVar9 = (&DAT_ffff0000be0de298)[uVar11 * 0x14];
      lVar13 = (&DAT_ffff0000be0de2a0)[uVar11 * 10];
      if (*puVar15 <= (uint)(&DAT_ffff0000be0de2ac)[uVar11 * 0x14]) {
        FUN_ffff0000be05fd98
                  (s__Thermal_Error__LVTS_TC_d__domin_ffff0000be0b4713,uVar11 & 0xffffffff);
        FUN_ffff0000be05fd98(s__Thermal_Use_the_sensor_point_0_a_ffff0000be0a3a6a);
        uVar7 = 0;
      }
      FUN_ffff0000be05fd98
                (s__Thermal__s___in_tc_d___the_domi_ffff0000be0bea00,
                 s_lvts_set_tc_trigger_hw_protect_ffff0000be0b6cd0,uVar11 & 0xffffffff,
                 (&DAT_ffff0000be0de270)[uVar11 * 0x14 + uVar7]);
      iVar6 = (int)uVar11;
      if (*puVar15 == 0) {
        uVar8 = 0;
        if (iVar6 != 0) goto code_r0xffff0000be04b1bc;
LAB_ffff0000be04b03c:
        FUN_ffff0000be04c2dc(0,uVar8);
        FUN_ffff0000be04c2c0((&DAT_ffff0000be0de2a8)[uVar11 * 0x14]);
      }
      else {
        uVar7 = 0;
        uVar8 = 0;
        do {
          lVar10 = (long)((&DAT_ffff0000be126150)[(uint)puVar12[uVar7]] +
                          (&DAT_ffff0000be0de2a8)[uVar11 * 0x14] + DAT_ffff0000be1260dc * -500);
          uVar2 = 0;
          if (lVar10 != 0) {
            uVar2 = (uint)(((long)(int)(&DAT_ffff0000be126150)[(uint)puVar12[uVar7]] << 0xe) /
                          lVar10);
          }
          uVar2 = uVar2 & 0xffff;
          FUN_ffff0000be05fd98
                    (s__Thermal__s_msr_raw____u_temp__d_ffff0000be0b9010,
                     s_lvts_temp_to_raw_ffff0000be0b4768,uVar2);
          if (uVar8 <= uVar2) {
            uVar8 = uVar2;
          }
          uVar7 = uVar7 + 1;
        } while (uVar7 < *puVar15);
        if (iVar6 == 0) goto LAB_ffff0000be04b03c;
code_r0xffff0000be04b1bc:
        if (iVar6 == 1) {
          FUN_ffff0000be04c2dc(1,uVar8);
        }
      }
      lVar13 = lVar13 + (ulong)uVar9;
      *(uint *)(lVar13 + 0xc0) = *(uint *)(lVar13 + 0xc0) | 0x3fff;
      *(undefined4 *)(lVar13 + 0xc) = 0;
      *(uint *)(lVar13 + 0xc0) = *(uint *)(lVar13 + 0xc0) & 0xfff0ffff | 0x10000;
      *(uint *)(lVar13 + 0xcc) = uVar8;
      *(undefined4 *)(lVar13 + 0xc) = 0x80000000;
      *(uint *)(lVar13 + 0xc0) = *(uint *)(lVar13 + 0xc0) & 0xffff0000;
    }
    uVar11 = uVar11 + 1;
    puVar12 = puVar12 + 0x14;
  } while (uVar11 != 4);
  if (DAT_ffff0000be0de290 != 0) {
    uVar11 = (ulong)DAT_ffff0000be0de294;
    if (DAT_ffff0000be0de294 == 0) {
      uVar9 = 0x200;
    }
    else {
      if (DAT_ffff0000be0de294 == 1) {
        uVar7 = 0;
        uVar9 = 0x200;
      }
      else {
        uVar9 = 0;
        uVar7 = uVar11 & 0xfffffffe;
        uVar8 = 0x200;
        uVar17 = 0;
        do {
          uVar2 = 1 << (ulong)((uint)uVar17 & 0x1f);
          uVar1 = uVar17 + 2;
          uVar3 = 1 << (ulong)((uint)uVar17 + 1 & 0x1f);
          if ((&DAT_ffff0000be0de280)[uVar17] != 1) {
            uVar2 = 0;
          }
          if ((&DAT_ffff0000be0de284)[uVar17] != 1) {
            uVar3 = 0;
          }
          uVar8 = uVar2 | uVar8;
          uVar9 = uVar3 | uVar9;
          uVar17 = uVar1;
        } while (uVar7 != uVar1);
        uVar9 = uVar9 | uVar8;
        if (uVar7 == uVar11) goto LAB_ffff0000be04b2a0;
      }
      do {
        uVar8 = 1 << (ulong)((uint)uVar7 & 0x1f);
        uVar17 = uVar7 + 1;
        if ((&DAT_ffff0000be0de280)[uVar7] != 1) {
          uVar8 = 0;
        }
        uVar9 = uVar8 | uVar9;
        uVar7 = uVar17;
      } while (uVar11 != uVar17);
    }
LAB_ffff0000be04b2a0:
    *(uint *)(DAT_ffff0000be0de2a0 + (ulong)DAT_ffff0000be0de298) = uVar9;
    FUN_ffff0000be05fd98
              (s__Thermal__s_tc_num__d_value_in_L_ffff0000be0a6226,
               s_lvts_enable_sensing_points_ffff0000be0a73f3,0);
  }
  if (DAT_ffff0000be0de2e0 != 0) {
    uVar11 = (ulong)DAT_ffff0000be0de2e4;
    if (DAT_ffff0000be0de2e4 == 0) {
      uVar9 = 0x200;
    }
    else {
      if (DAT_ffff0000be0de2e4 == 1) {
        uVar7 = 0;
        uVar9 = 0x200;
      }
      else {
        uVar9 = 0;
        uVar7 = uVar11 & 0xfffffffe;
        uVar8 = 0x200;
        uVar17 = 0;
        do {
          uVar2 = 1 << (ulong)((uint)uVar17 & 0x1f);
          uVar1 = uVar17 + 2;
          uVar3 = 1 << (ulong)((uint)uVar17 + 1 & 0x1f);
          if ((&DAT_ffff0000be0de2d0)[uVar17] != 1) {
            uVar2 = 0;
          }
          if ((&DAT_ffff0000be0de2d4)[uVar17] != 1) {
            uVar3 = 0;
          }
          uVar8 = uVar2 | uVar8;
          uVar9 = uVar3 | uVar9;
          uVar17 = uVar1;
        } while (uVar7 != uVar1);
        uVar9 = uVar9 | uVar8;
        if (uVar7 == uVar11) goto LAB_ffff0000be04b378;
      }
      do {
        uVar8 = 1 << (ulong)((uint)uVar7 & 0x1f);
        uVar17 = uVar7 + 1;
        if ((&DAT_ffff0000be0de2d0)[uVar7] != 1) {
          uVar8 = 0;
        }
        uVar9 = uVar8 | uVar9;
        uVar7 = uVar17;
      } while (uVar11 != uVar17);
    }
LAB_ffff0000be04b378:
    *(uint *)(DAT_ffff0000be0de2f0 + (ulong)DAT_ffff0000be0de2e8) = uVar9;
    FUN_ffff0000be05fd98
              (s__Thermal__s_tc_num__d_value_in_L_ffff0000be0a6226,
               s_lvts_enable_sensing_points_ffff0000be0a73f3,1);
  }
  if (DAT_ffff0000be0de330 != 0) {
    uVar11 = (ulong)DAT_ffff0000be0de334;
    if (DAT_ffff0000be0de334 == 0) {
      uVar9 = 0x200;
    }
    else {
      if (DAT_ffff0000be0de334 == 1) {
        uVar7 = 0;
        uVar9 = 0x200;
      }
      else {
        uVar9 = 0;
        uVar7 = uVar11 & 0xfffffffe;
        uVar8 = 0x200;
        uVar17 = 0;
        do {
          uVar2 = 1 << (ulong)((uint)uVar17 & 0x1f);
          uVar1 = uVar17 + 2;
          uVar3 = 1 << (ulong)((uint)uVar17 + 1 & 0x1f);
          if ((&DAT_ffff0000be0de320)[uVar17] != 1) {
            uVar2 = 0;
          }
          if ((&DAT_ffff0000be0de324)[uVar17] != 1) {
            uVar3 = 0;
          }
          uVar8 = uVar2 | uVar8;
          uVar9 = uVar3 | uVar9;
          uVar17 = uVar1;
        } while (uVar7 != uVar1);
        uVar9 = uVar9 | uVar8;
        if (uVar7 == uVar11) goto LAB_ffff0000be04b450;
      }
      do {
        uVar8 = 1 << (ulong)((uint)uVar7 & 0x1f);
        uVar17 = uVar7 + 1;
        if ((&DAT_ffff0000be0de320)[uVar7] != 1) {
          uVar8 = 0;
        }
        uVar9 = uVar8 | uVar9;
        uVar7 = uVar17;
      } while (uVar11 != uVar17);
    }
LAB_ffff0000be04b450:
    *(uint *)(DAT_ffff0000be0de340 + (ulong)DAT_ffff0000be0de338) = uVar9;
    FUN_ffff0000be05fd98
              (s__Thermal__s_tc_num__d_value_in_L_ffff0000be0a6226,
               s_lvts_enable_sensing_points_ffff0000be0a73f3,2);
  }
  if (DAT_ffff0000be0de380 == 0) goto LAB_ffff0000be04b548;
  uVar11 = (ulong)DAT_ffff0000be0de384;
  if (DAT_ffff0000be0de384 == 0) {
    uVar9 = 0x200;
  }
  else {
    if (DAT_ffff0000be0de384 == 1) {
      uVar7 = 0;
      uVar9 = 0x200;
    }
    else {
      uVar9 = 0;
      uVar7 = uVar11 & 0xfffffffe;
      uVar8 = 0x200;
      uVar17 = 0;
      do {
        uVar2 = 1 << (ulong)((uint)uVar17 & 0x1f);
        uVar1 = uVar17 + 2;
        uVar3 = 1 << (ulong)((uint)uVar17 + 1 & 0x1f);
        if ((&DAT_ffff0000be0de370)[uVar17] != 1) {
          uVar2 = 0;
        }
        if ((&DAT_ffff0000be0de374)[uVar17] != 1) {
          uVar3 = 0;
        }
        uVar8 = uVar2 | uVar8;
        uVar9 = uVar3 | uVar9;
        uVar17 = uVar1;
      } while (uVar7 != uVar1);
      uVar9 = uVar9 | uVar8;
      if (uVar7 == uVar11) goto LAB_ffff0000be04b52c;
    }
    do {
      uVar8 = 1 << (ulong)((uint)uVar7 & 0x1f);
      uVar17 = uVar7 + 1;
      if ((&DAT_ffff0000be0de370)[uVar7] != 1) {
        uVar8 = 0;
      }
      uVar9 = uVar8 | uVar9;
      uVar7 = uVar17;
    } while (uVar11 != uVar17);
  }
LAB_ffff0000be04b52c:
  *(uint *)(DAT_ffff0000be0de390 + (ulong)DAT_ffff0000be0de388) = uVar9;
  FUN_ffff0000be05fd98
            (s__Thermal__s_tc_num__d_value_in_L_ffff0000be0a6226,
             s_lvts_enable_sensing_points_ffff0000be0a73f3,3);
LAB_ffff0000be04b548:
  if (DAT_ffff0000be0de290 != 0) {
    *(undefined4 *)((ulong)DAT_ffff0000be0de298 + DAT_ffff0000be0de2a0 + 0xf0) = 0x555;
  }
  if (DAT_ffff0000be0de2e0 != 0) {
    *(undefined4 *)((ulong)DAT_ffff0000be0de2e8 + DAT_ffff0000be0de2f0 + 0xf0) = 0x555;
  }
  if (DAT_ffff0000be0de330 != 0) {
    *(undefined4 *)((ulong)DAT_ffff0000be0de338 + DAT_ffff0000be0de340 + 0xf0) = 0x555;
  }
  if (DAT_ffff0000be0de380 != 0) {
    *(undefined4 *)((ulong)DAT_ffff0000be0de388 + DAT_ffff0000be0de390 + 0xf0) = 0x555;
  }
  iVar6 = FUN_ffff0000be019910();
  FUN_ffff0000be05fd98
            (s__PROFILE_______s_takes__d_ms_ffff0000be0aa9d0,s_lvts_thermal_init_ffff0000be0a9646,
             iVar6 - iVar5);
  FUN_ffff0000be05fd98(s__Thermal_thermal_init__thermal_i_ffff0000be0b1250);
  FUN_ffff0000be04bdb0();
  FUN_ffff0000be04c188();
  return;
}


